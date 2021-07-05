package com.diemerson.mobilefood.api.assembler;

import com.diemerson.mobilefood.api.model.input.RestauranteInput;
import com.diemerson.mobilefood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestauranteInputAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public RestauranteInput toInput(Restaurante restaurante){
        return modelMapper.map(restaurante, RestauranteInput.class);
    }
}
