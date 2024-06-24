package net.val.api.dtos.medicoDto;

import jakarta.validation.constraints.NotNull;
import net.val.api.dtos.enderecoDto.DadosEndereco;

public record DadosAtualizacaoMedico(
        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco
) {
}
