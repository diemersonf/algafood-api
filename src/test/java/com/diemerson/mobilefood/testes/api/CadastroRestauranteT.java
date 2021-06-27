package com.diemerson.mobilefood.testes.api;

import com.diemerson.mobilefood.domain.model.Cozinha;
import com.diemerson.mobilefood.domain.model.Restaurante;
import com.diemerson.mobilefood.domain.repository.CozinhaRepository;
import com.diemerson.mobilefood.domain.repository.RestauranteRepository;
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

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteT {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @LocalServerPort
    private int port;

    private Restaurante zeBanana = new Restaurante();

    private Integer countRestaurantesNoBd;

    @Before
    public void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port=port;
        RestAssured.basePath="/restaurantes";

        databaseCleaner.clearTables();
        prepararDados();
    }

    @Test
    public void deveRetornarStatus201_QuandoCadastrarRestaurante(){
        String json = ResourceUtils.getContentFromResource("/json/deveRetornarStatus201_QuandoCadastrarRestaurante.json");
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(json)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void deveRetornarStatus201_QuandoCadastrarRestauranteComTaxaFreteZeroENomeConterFreteGratis(){
        String json = ResourceUtils.getContentFromResource(
                "/json/deveRetornarStatus201_QuandoCadastrarRestauranteComTaxaFreteZeroENomeConterFreteGratis.json");
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(json)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void deveRetornarStatus400_QuandoCadastrarRestauranteComCozinhaInexistente(){
        String json = ResourceUtils.getContentFromResource("/json/deveRetornarStatus400_QuandoCadastrarRestauranteComCozinhaInexistente.json");
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(json)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("detail", containsString("Não existe um cadastro de cozinha com código"));
    }

    @Test
    public void deveRetornarStatus400EMenssagemDeFreteMaiorQueZero_QuandoCadastrarRestauranteComValorDaTaxaFreteNegativo(){
        String json = ResourceUtils.getContentFromResource(
                "/json/deveRetornarStatus400EMenssagemDeFreteMaiorQueZero_QuandoCadastrarRestauranteComValorDaTaxaFreteNegativo.json");
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(json)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("userMessage", containsString("Taxa Frete do restaurante precisar ter uma valor maior do que zero"));
    }

    @Test
    public void deveRetornarStatus400ComMenssagemDeNomeComFreteGratis_QuandoCadastrarRestauranteComValorDaTaxaZero(){
        String json = ResourceUtils.getContentFromResource(
                "/json/deveRetornarStatus400ComMenssagemDeNomeComFreteGratis_QuandoCadastrarRestauranteComValorDaTaxaZero.json");
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(json)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("userMessage", containsString("nome deve conter Frete Grátis"));
    }

    @Test
    public void deveRetornarStatus400ComMenssagemDeNomeObrigatorio_QuandoCadastrarRestauranteSemInformarNome(){
        String json = ResourceUtils.getContentFromResource(
                "/json/deveRetornarStatus400ComMenssagemDeNomeObrigatorio_QuandoCadastrarRestauranteSemInformarNome.json");
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(json)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("userMessage", containsString("Nome do restaurante é obrigatório"));
    }

    @Test
    public void deveRetornarStatus200_QuandoConsultarRestauranteExistente(){
        given()
            .pathParam("restauranteId", zeBanana.getId())
            .accept(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo(zeBanana.getNome()));
    }

    private void prepararDados(){
        Cozinha cozinhaTailandesa = new Cozinha();
        cozinhaTailandesa.setNome("Tailandesa");
        cozinhaRepository.save(cozinhaTailandesa);

        zeBanana.setNome("Ze Banana");
        zeBanana.setTaxaFrete(BigDecimal.valueOf(4.5));
        zeBanana.setCozinha(cozinhaTailandesa);
        restauranteRepository.save(zeBanana);

        Cozinha cozinhaAmericana = new Cozinha();
        cozinhaAmericana.setNome("Americana");
        cozinhaRepository.save(cozinhaAmericana);

        Restaurante ariga = new Restaurante();
        ariga.setNome("Ze Banana");
        ariga.setTaxaFrete(BigDecimal.valueOf(2));
        ariga.setCozinha(cozinhaAmericana);
        restauranteRepository.save(ariga);

        countRestaurantesNoBd = Math.toIntExact(restauranteRepository.count());
    }
}
