package com.diemerson.mobilefood.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EstadoIdInput {
    @Valid
    @NotNull
    public Long id;
}
