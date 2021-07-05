package com.diemerson.mobilefood.api.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteModel {

    private Long id;
    private String nome;
    private Boolean status;
    private BigDecimal taxaFrete;
    private CozinhaModel cozinha;
}
