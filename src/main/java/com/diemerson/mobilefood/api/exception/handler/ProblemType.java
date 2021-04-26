package com.diemerson.mobilefood.api.exception.handler;

import lombok.Getter;

@Getter
public enum ProblemType {

    REQUISICAO_IVALIDA("/requisicao-invalida", "Requisição Inválida"),
    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não Encontrada"),
    ERRO_NEGOCIO("/erro-negocio", "Violacao Regra de Negócio"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em Uso"),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro Inválido");

    private String title;
    private String uri;

    ProblemType(String path, String type){
        this.uri = "https://algafood.com.br/api/help" + path;
        this.title = type;
    }
}
