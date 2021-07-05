package com.diemerson.mobilefood.testes.api;

import com.diemerson.mobilefood.domain.model.FormaPagamento;
import com.diemerson.mobilefood.domain.repository.FormaPagamentoRepository;
import com.diemerson.mobilefood.util.DatabaseCleaner;
import com.diemerson.mobilefood.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.StringContains.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroFormaPagamentoT {
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @LocalServerPort
    private int port;

    private Integer countFormaPagamentosCountBd = 0;
    private FormaPagamento formaPagamentoValeAlimentacao = new FormaPagamento();

    @Before
    public void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port=port;
        RestAssured.basePath="/formasdepagametos";

        databaseCleaner.clearTables();
        prepararDados();
    }

    @Test
    public void deveRetornarStatus200EQuantidadeDeCadastrosExistentesNoBd_QuandoConsultarListaDeFormasDePagamentos(){
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
            .when()
                .get()
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("", hasSize(countFormaPagamentosCountBd));
    }

    @Test
    public void deveRetornarStatus201_QuandoCadastrarFormaDePagamentoComSucesso(){
        String json = ResourceUtils.getContentFromResource(
                "/json/FormasDePagamentoTests/deveRetornarStatus201_QuandoCadastrarFormaDePagamentoComSucesso.json");
        given()
            .body(json)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void deveRetornarValeAlimentacaoEStatus200_QuandoBuscarFormaDePagamentoComId1(){
        given()
            .pathParam("formaPagamentoId", 1)
            .accept(ContentType.JSON)
        .when()
            .get("/{formaPagamentoId}")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("descricao", containsString("Vale Alimentação"));
    }

    @Test
    public void deveRetornarStatus404EMenssagemRecursoNaoEncontrado_QuandoDeletarFormaDePagamentoComIdInexistente(){
        given()
            .pathParam("formaPagamentoId", countFormaPagamentosCountBd+1)
            .accept(ContentType.JSON)
        .when()
            .delete("/{formaPagamentoId}")
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("title", containsString("Recurso não Encontrado"));
    }

    private void prepararDados(){
        formaPagamentoValeAlimentacao.setDescricao("Vale Alimentação");
        formaPagamentoRepository.save(formaPagamentoValeAlimentacao);

        FormaPagamento valeRefeicao = new FormaPagamento();
        valeRefeicao.setDescricao("Vale Refeição");
        formaPagamentoRepository.save(valeRefeicao);

        countFormaPagamentosCountBd = Math.toIntExact(formaPagamentoRepository.count());
    }
}
