package com.diemerson.mobilefood.api.model;

import com.diemerson.mobilefood.domain.model.Estado;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeModel {
    private Long id;
    private String nome;
    private EstadoModel estado;
}
