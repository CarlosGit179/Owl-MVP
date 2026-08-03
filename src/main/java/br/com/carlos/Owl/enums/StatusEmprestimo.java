package br.com.carlos.Owl.enums;

import lombok.Getter;

/**
 * Enumeração que representa os possíveis status de um empréstimo.
 * <p>
 * Contém os status "Em andamento" e "Devolvido", cada um com uma descrição associada.
 * </p>
 */
@Getter
public enum StatusEmprestimo {

    EM_ANDAMENTO("Em andamento"),
    DEVOLVIDO("Devolvido");

    private final String descricao;

    /**
     * Construtor da enumeração StatusEmprestimo.
     *
     * @param descricao Descrição do status do empréstimo.
     */
    StatusEmprestimo(String descricao) {
        this.descricao = descricao;
    }

}
