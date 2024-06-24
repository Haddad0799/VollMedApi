package net.val.api.dtos.medicoDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import net.val.api.dtos.enderecoDto.DadosEndereco;

public record DadosCadastraisMedico(
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email,
        String telefone,
        @NotBlank
        @Pattern(regexp = "\\d{4,6}")
        String crm,
        @NotNull
        String especialidade,
        @NotNull
        @Valid
        DadosEndereco endereco
) {


}
