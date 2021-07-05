package com.diemerson.mobilefood.api.assembler;

import com.diemerson.mobilefood.api.model.EstadoModel;
import com.diemerson.mobilefood.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class EstadoModelAssembler {
    @Autowired
    private ModelMapper modelMapper;

    public EstadoModel toModel(Estado estado){
        return modelMapper.map(estado, EstadoModel.class);
    }

    public List<EstadoModel> toCollectionModel(List<Estado> estados){
        return estados.stream()
                .map(estado -> toModel(estado))
                .collect(Collectors.toList());
    }
}
