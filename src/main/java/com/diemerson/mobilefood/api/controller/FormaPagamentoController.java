package com.diemerson.mobilefood.api.controller;

import com.diemerson.mobilefood.api.assembler.FormaPagamentoAssembler;
import com.diemerson.mobilefood.api.assembler.FormaPagamentoDesassembler;
import com.diemerson.mobilefood.api.model.FormaPagamentoModel;
import com.diemerson.mobilefood.api.model.input.FormaPagamentoInput;
import com.diemerson.mobilefood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.diemerson.mobilefood.domain.model.FormaPagamento;
import com.diemerson.mobilefood.domain.service.CadastroFormaPagametoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value =  "/formasdepagametos")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoAssembler formaPagamentoAssembler;

    @Autowired
    private FormaPagamentoDesassembler formaPagamentoDesassembler;

    @Autowired
    private CadastroFormaPagametoService cadastroFormaPagametoService;

    @GetMapping
    public List<FormaPagamentoModel> listar(){
        return formaPagamentoAssembler.toCollectionModel(cadastroFormaPagametoService.buscarTodos());
    }

    @GetMapping("/{formaDePagamentoId}")
    public FormaPagamentoModel buscarPorId(@PathVariable Long formaDePagamentoId){
        return formaPagamentoAssembler.toModel(cadastroFormaPagametoService.buscarOuFalhar(formaDePagamentoId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel salvar(@RequestBody FormaPagamentoInput formaPagamentoInput){
        FormaPagamento formaPagamento = formaPagamentoDesassembler.toObjectDomain(formaPagamentoInput);
        return formaPagamentoAssembler.toModel(cadastroFormaPagametoService.salvar(formaPagamento));
    }
    
    @PutMapping("/{formaPagamentoId}")
    public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId, @RequestBody FormaPagamentoInput formaPagamentoInput){
        FormaPagamento formaPagamentoAtual = cadastroFormaPagametoService.buscarOuFalhar(formaPagamentoId);
        formaPagamentoDesassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);
        return formaPagamentoAssembler.toModel(cadastroFormaPagametoService.salvar(formaPagamentoAtual));
    }

    @DeleteMapping("/{formaPagamentoId}")

    public void deletar(@PathVariable Long formaPagamentoId){
        cadastroFormaPagametoService.excluir(formaPagamentoId);
    }
}
