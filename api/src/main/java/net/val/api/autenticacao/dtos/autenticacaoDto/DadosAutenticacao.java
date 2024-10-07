package net.val.api.autenticacao.dtos.autenticacaoDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DadosAutenticacao(
        String login,
        @Schema(description = "Senha do usuário", example = "****", format = "password")
        String senha
) {
}
