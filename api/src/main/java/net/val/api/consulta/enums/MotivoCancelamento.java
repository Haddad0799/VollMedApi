package net.val.api.consulta.enums;

import lombok.Getter;
import net.val.api.infra.exceptions.consultaExceptions.cancelamentoExceptions.MotivoCancelamentoIncorretoException;

@Getter
public enum MotivoCancelamento {

    PACIENTE_DESISTIU("Paciente Desistiu"),
    MEDICO_CANCELOU("Médico Cancelou"),
    OUTROS_MOTIVOS("Outros Motivos");

    private final String descricao;

    MotivoCancelamento(String descricao) {
        this.descricao = descricao;
    }

    public static MotivoCancelamento fromDescricao(String descricao) {
        for (MotivoCancelamento motivo : MotivoCancelamento.values()) {
            if (motivo.descricao.equalsIgnoreCase(descricao)) {
                return motivo;
            }
        }
        throw new MotivoCancelamentoIncorretoException(descricao);
    }
}
