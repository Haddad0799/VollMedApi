package net.val.api.dtos.pacienteDto;

import jakarta.validation.constraints.NotNull;
import net.val.api.dtos.enderecoDto.DadosEndereco;

public record DadosAtualizacaoPaciente(
        @NotNull
        Long id,
        String nome,
        String telefone,
        Double peso,
        DadosEndereco endereco
) {
}
