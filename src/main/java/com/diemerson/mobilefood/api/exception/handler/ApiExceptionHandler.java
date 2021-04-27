package com.diemerson.mobilefood.api.exception.handler;


import com.diemerson.mobilefood.domain.exception.EntidadeEmUsoException;
import com.diemerson.mobilefood.domain.exception.EntidadeNaoEncontradaException;
import com.diemerson.mobilefood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        ResponseEntity<Object> origimRootCase = validRootCause(headers, status, request, rootCause, ex);
        return origimRootCase;
    }

    private ResponseEntity<Object> handlePropertyBindingException(Throwable ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String property = joinPath((MismatchedInputException) ex);

        ProblemType problemType = ProblemType.REQUISICAO_IVALIDA;
        String detail = String.format("A propriedade '%s' não é uma propriedade válida para a entidade restaurante. " +
                        "Revise sua requisição!!! ", property);

        Problem problem = createProblemBuilder(status, problemType, detail).build();
        return handleExceptionInternal((PropertyBindingException) ex, problem, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
                                                                 HttpStatus status, WebRequest request) {
        String property = joinPath((MismatchedInputException) ex);

        ProblemType problemType = ProblemType.REQUISICAO_IVALIDA;
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', " +
                "que é de um tipo inválido. Corriga e informe um valor compatível com o tipo '%s'",
                property, ex.getValue(), ex.getTargetType().getSimpleName());
        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request){

        ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
        HttpStatus status = HttpStatus.NOT_FOUND;
        String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', que é de um tipo inválido. " +
                        "Corrija e informe um valor compatível com o tipo %s.",
                ex.getParameter().getParameterName(), ex.getValue(), ex.getRequiredType().getTypeName());
        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(
            EntidadeNaoEncontradaException ex, WebRequest request){

        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem problem = createProblemBuilder(status, problemType, ex.getMessage()).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request){
        ProblemType problemType = ProblemType.ERRO_NEGOCIO;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Problem problem = createProblemBuilder(status, problemType, ex.getMessage()).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request){
        ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
        HttpStatus status = HttpStatus.CONFLICT;
        Problem problem = createProblemBuilder(status, problemType, ex.getMessage()).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (body == null) {
            body = Problem.builder()
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .title((String) body)
                    .status(status.value())
                    .build();
        }

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail ){
        return Problem.builder()
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }

    private String joinPath(MismatchedInputException ex) {
        return ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));
    }

    private ResponseEntity<Object> validRootCause(HttpHeaders headers, HttpStatus status, WebRequest request, Throwable rootCause, HttpMessageNotReadableException ex) {
        ResponseEntity<Object> exception = null;

        if(rootCause instanceof InvalidFormatException){
            exception = handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException){
            exception = handlePropertyBindingException(rootCause, headers, status, request);
        } else {
            ProblemType problemType = ProblemType.REQUISICAO_IVALIDA;
            String detail = "Algo na requisição está incorreto. Verifique!";
            Problem problem = createProblemBuilder(status, problemType, detail).build();

            exception = handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
        }
        return exception;
    }
}

