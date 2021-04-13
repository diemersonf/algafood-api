package com.diemerson.mobilefood.api.exception.handler;


import com.diemerson.mobilefood.domain.exception.EntidadeEmUsoException;
import com.diemerson.mobilefood.domain.exception.EntidadeNaoEncontradaException;
import com.diemerson.mobilefood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
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
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if(rootCause instanceof InvalidFormatException){
            return handleInvalideFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException){
            return handlePropertyBindingException(rootCause, headers, status, request);
        }

        ProblemType problemType = ProblemType.REQUISICAO_INCORRETA;
        String detail = "Algo na requisição está incorreto. Verifique!";
        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);

    }

    private ResponseEntity<Object> handlePropertyBindingException(Throwable ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String property = ((PropertyBindingException) ex).getPath().stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining());

        ProblemType problemType = ProblemType.REQUISICAO_INCORRETA;
        String detail = String.format("A propriedade '%s' não é uma propriedade válida para a entidade restaurante. " +
                        "Revise sua requisição!!! ", property);

        Problem problem = createProblemBuilder(status, problemType, detail).build();
        return handleExceptionInternal((PropertyBindingException) ex, problem, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleInvalideFormatException(InvalidFormatException ex, HttpHeaders headers,
                                                                 HttpStatus status, WebRequest request) {
        String path = ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));

        ProblemType problemType = ProblemType.REQUISICAO_INCORRETA;
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', " +
                "que é de um tipo inválido. Corriga e informe um valor compatível com o tipo '%s'", 
                path, ex.getValue(), ex.getTargetType().getSimpleName());
        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(
            EntidadeNaoEncontradaException ex, WebRequest request){

        ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;
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
}

