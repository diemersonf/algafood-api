package com.diemerson.mobilefood.api.assembler;

import com.diemerson.mobilefood.api.model.input.EstadoInput;
import com.diemerson.mobilefood.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EstadoInputDesassembler {
    @Autowired
    private ModelMapper modelMapper;

    public Estado toObjectDomain(EstadoInput estadoInput){
        return modelMapper.map(estadoInput, Estado.class);
    }

    public void copyToDomainObject(EstadoInput estadoInput, Estado estado){
        modelMapper.map(estadoInput, estado);
    }
}
