package br.com.carlos.Owl.enums;

import lombok.Getter;

@Getter
public enum StatusEmprestimo {

    EM_ANDAMENTO("Em andamento"),
    DEVOLVIDO("Devolvido");

    private final String descricao;

    StatusEmprestimo(String descricao) {
        this.descricao = descricao;
    }

}
