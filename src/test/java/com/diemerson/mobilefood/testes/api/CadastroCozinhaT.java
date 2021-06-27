package com.diemerson.mobilefood.testes.api;

import com.diemerson.mobilefood.domain.model.Cozinha;
import com.diemerson.mobilefood.domain.repository.CozinhaRepository;
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

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroCozinhaT {
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @LocalServerPort
    private int port;

    private Integer countCozinhasBd = 0;
    private Cozinha cozinhaTailandesa = new Cozinha();

    @Before
    public void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port=port;
        RestAssured.basePath="/cozinhas";

        databaseCleaner.clearTables();
        prepararDados();
    }

    @Test
    public void deveRetornarStatus201_QuandoCadastrarCozinha(){
        String json = ResourceUtils.getContentFromResource("/json/cozinha-portuguesa.json");
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
    public void deveRetornarStatus200_QuandoConsultarCozinhas(){
        given()
            .accept(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void deveRetornarQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas(){
        enableLoggingOfRequestAndResponseIfValidationFails();

        given()
            .accept(ContentType.JSON)
        .when()
            .get()
        .then()
            .body("", hasSize(countCozinhasBd));
    }

    @Test
    public void deveRetornarCozinhaExistenteStatus200_QuandoConsultarCozinhaExistente(){
        given()
            .pathParam("cozinhaId", cozinhaTailandesa.getId())
            .accept(ContentType.JSON)
        .when()
            .get("/{cozinhaId}")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("nome", equalTo(cozinhaTailandesa.getNome()));
    }

    @Test
    public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente(){
        given()
            .pathParam("cozinhaId", countCozinhasBd+1)
            .accept(ContentType.JSON)
        .when()
            .get("/{cozinhaId}")
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("title", equalTo("Recurso não Encontrado"));
    }

    private void prepararDados(){
        cozinhaTailandesa.setNome("Tailandesa");
        cozinhaRepository.save(cozinhaTailandesa);

        Cozinha cozinha2 = new Cozinha();
        cozinha2.setNome("Americana");
        cozinhaRepository.save(cozinha2);

        countCozinhasBd = Math.toIntExact(cozinhaRepository.count());
    }


}
