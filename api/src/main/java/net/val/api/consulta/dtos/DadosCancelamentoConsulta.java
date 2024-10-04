package net.val.api.consulta.dtos;

import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoConsulta(

        @NotNull
        Long consultaId,
        @NotNull
        String motivoCancelamento
) {
}
