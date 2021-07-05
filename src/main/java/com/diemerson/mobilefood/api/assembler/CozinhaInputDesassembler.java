package com.diemerson.mobilefood.api.assembler;

import com.diemerson.mobilefood.api.model.input.CozinhaInput;
import com.diemerson.mobilefood.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CozinhaInputDesassembler {
    @Autowired
    private ModelMapper modelMapper;

    public Cozinha toDomainObject(CozinhaInput cozinhaInput){
        return modelMapper.map(cozinhaInput, Cozinha.class);
    }

    public void copyToDomainObject(CozinhaInput cozinhaInput, Cozinha cozinha){
        modelMapper.map(cozinhaInput, cozinha);
    }
}
