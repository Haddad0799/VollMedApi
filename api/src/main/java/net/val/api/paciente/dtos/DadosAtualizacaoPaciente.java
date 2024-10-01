package net.val.api.paciente.dtos;

import jakarta.validation.constraints.NotNull;
import net.val.api.endereco.dtos.DadosEndereco;

public record DadosAtualizacaoPaciente(
        @NotNull
        Long id,
        String nome,
        String telefone,
        Double peso,
        DadosEndereco endereco
) {
}
