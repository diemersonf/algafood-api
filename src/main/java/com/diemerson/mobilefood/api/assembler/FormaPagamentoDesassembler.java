package com.diemerson.mobilefood.api.assembler;

import com.diemerson.mobilefood.api.model.input.FormaPagamentoInput;
import com.diemerson.mobilefood.domain.model.FormaPagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormaPagamentoDesassembler {
    @Autowired
    private ModelMapper modelMapper;

    public FormaPagamento toObjectDomain(FormaPagamentoInput formaPagamentoInput){
        return modelMapper.map(formaPagamentoInput, FormaPagamento.class);
    }

    public void copyToDomainObject(FormaPagamentoInput formaPagamentoInput, FormaPagamento formaPagamento){
        modelMapper.map(formaPagamentoInput, formaPagamento);
    }
}
