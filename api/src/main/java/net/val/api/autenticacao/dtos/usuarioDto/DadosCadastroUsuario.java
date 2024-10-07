package net.val.api.autenticacao.dtos.usuarioDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroUsuario(
        @NotNull
        String login,
        @NotNull
        @Schema(description = "Senha do usuário", example = "****", format = "password")
        String senha) {
}
