package com.diemerson.mobilefood.api.assembler;

import com.diemerson.mobilefood.api.model.CidadeModel;
import com.diemerson.mobilefood.api.model.input.CidadeInput;
import com.diemerson.mobilefood.api.model.input.EstadoIdInput;
import com.diemerson.mobilefood.domain.model.Cidade;
import com.diemerson.mobilefood.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CidadeInputDesassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Cidade toDomainObject(CidadeInput cidadeInput){
        return modelMapper.map(cidadeInput, Cidade.class);
    }

    public void copyToDomainObject(CidadeInput cidadeInput, Cidade cidade){
        /*
            Evitar Exception
              Error during managed flush [org.hibernate.HibernateException:
                    identifier of an instance of com.diemerson.mobilefood.domain.model.Estado was altered from 2 to 3]
         */
        cidade.setEstado(new Estado());
        modelMapper.map(cidadeInput, cidade);
    }
}
