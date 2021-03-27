package com.algaworks.algafood.api.exception.handler;

import lombok.Getter;

@Getter
public enum ProblemType {

    REQUISICAO_INCORRETA("/requisicao-invalida", "Requisição Inválida"),
    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não Encontrada"),
    ERRO_NEGOCIO("/erro-negocio", "Violacao Regra de Negócio"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em Uso");

    private String title;
    private String uri;

    ProblemType(String path, String type){
        this.uri = "https://algafood.com.br/api/help" + path;
        this.title = type;
    }
}
