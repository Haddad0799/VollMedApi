package net.val.api.domain;

import lombok.Getter;

@Getter
public enum TipoSanguineo {
    A_POSITIVO("A+"),
    B_POSITIVO("B+"),
    AB_POSITIVO("AB+"),
    O_POSITIVO("O+"),
    A_NEGATIVO("A-"),
    B_NEGATIVO("B-"),
    AB_NEGATIVO("AB-"),
    O_NEGATIVO("O-");

    private final String descricao;

    TipoSanguineo(String descricao) {
        this.descricao = descricao;
    }

    public static TipoSanguineo fromDescricao(String descricao) {
        for (TipoSanguineo tipo : TipoSanguineo.values()) {
            if (tipo.getDescricao().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo sanguíneo inválido: " + descricao);
    }
}
