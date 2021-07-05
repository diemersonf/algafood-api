package com.diemerson.mobilefood.domain.service;

import com.diemerson.mobilefood.domain.exception.EntidadeEmUsoException;
import com.diemerson.mobilefood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.diemerson.mobilefood.domain.exception.RestauranteNaoEncontradoException;
import com.diemerson.mobilefood.domain.model.FormaPagamento;
import com.diemerson.mobilefood.domain.repository.FormaPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroFormaPagametoService {

    private static final String MSG_FORMA_PAGAMENTO_EM_USO = "A forma de pagamento de código %d está em uso e não pode ser excluída.";

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    public List<FormaPagamento> buscarTodos() {
        return formaPagamentoRepository.findAll();
    }
    
    public FormaPagamento buscarOuFalhar(Long formaPagamentoId){
        return formaPagamentoRepository.findById(formaPagamentoId)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(formaPagamentoId));
    }

    public FormaPagamento salvar(FormaPagamento formaPagamento) {
        return formaPagamentoRepository.save(formaPagamento);
    }

    @Transactional
    public void excluir(Long formaPagamentoId) {
        try {
            formaPagamentoRepository.deleteById(formaPagamentoId);
            formaPagamentoRepository.flush();
        }catch (EmptyResultDataAccessException ex){
            throw new FormaPagamentoNaoEncontradaException(formaPagamentoId);
        } catch (DataIntegrityViolationException exception){
            throw new EntidadeEmUsoException(
                    String.format(MSG_FORMA_PAGAMENTO_EM_USO, formaPagamentoId));
        }
    }
}
