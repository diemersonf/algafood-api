package com.diemerson.mobilefood.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteInput {
    @NotNull
    private String nome;

    @PositiveOrZero
    @NotNull
    private BigDecimal taxaFrete;

    @NotNull
    @Valid
    private CozinhaIdInput cozinha;
}
