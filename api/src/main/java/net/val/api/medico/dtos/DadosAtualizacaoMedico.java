package net.val.api.medico.dtos;

import jakarta.validation.constraints.NotNull;
import net.val.api.endereco.dtos.DadosEndereco;

public record DadosAtualizacaoMedico(
        @NotNull
        Long id,
        String telefone,
        DadosEndereco endereco
) {
}
