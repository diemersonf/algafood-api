package com.diemerson.mobilefood.api.assembler;

import com.diemerson.mobilefood.api.model.input.RestauranteInput;
import com.diemerson.mobilefood.domain.model.Cozinha;
import com.diemerson.mobilefood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestauranteInputDesassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Restaurante toDomainObject(RestauranteInput restauranteInput) {
        return modelMapper.map(restauranteInput, Restaurante.class);
    }
    public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante){
        /*
            Evitar Exception
            org.springframework.orm.jpa.JpaSystemException:
            identifier of an instance of com.diemerson.mobilefood.domain.model.Cozinha was altered from 1 to 2
         */
        restaurante.setCozinha(new Cozinha());
        modelMapper.map(restauranteInput, restaurante);
    }
}
