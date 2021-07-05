package com.diemerson.mobilefood.testes.integracao;

import com.diemerson.mobilefood.domain.exception.CozinhaNaoEncontradaException;
import com.diemerson.mobilefood.domain.exception.EntidadeEmUsoException;
import com.diemerson.mobilefood.domain.model.Cozinha;
import com.diemerson.mobilefood.domain.service.CadastroCozinhaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CozinhaIntegracaoTest {
    @Autowired
    CadastroCozinhaService cozinhaService;

    @Test
    public void deveDarSucesso_QuandoCadastrarCozinhaCorretamente(){
        // Cenario
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome("Chinesa");

        // Ação
        novaCozinha = cozinhaService.salvar(novaCozinha);

        // Validação
        assertThat(novaCozinha).isNotNull();
        assertThat(novaCozinha.getId()).isNotNull();
    }

    @Test(expected = ConstraintViolationException.class)
    public void deveFalhar_QuandoCadastrarCozinhaSemNome(){
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome(null);

        novaCozinha = cozinhaService.salvar(novaCozinha);
    }

    @Test(expected = EntidadeEmUsoException.class )
    public void deveFalhar_QuandoExcluirCozinhaEmUso(){
        cozinhaService.excluir(3L);
    }

    @Test(expected = CozinhaNaoEncontradaException.class)
    public void deveFalhar_QuandoExcluirCozinhaInexistente(){
        cozinhaService.excluir(6L);
    }
}
