package net.val.api.dtos.usuarioDto;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroUsuario(
        @NotNull
        String login,
        @NotNull
        String senha) {
}
