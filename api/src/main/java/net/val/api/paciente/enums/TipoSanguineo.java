package net.val.api.paciente.enums;

import lombok.Getter;
import net.val.api.infra.exceptions.tipoSanguineoExceptions.TipoSanguineoInvalidoException;

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

    public static TipoSanguineo fromTipo(String descricao) {
        for (TipoSanguineo tipo : TipoSanguineo.values()) {
            if (tipo.getDescricao().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        throw new TipoSanguineoInvalidoException( descricao);
    }
}
