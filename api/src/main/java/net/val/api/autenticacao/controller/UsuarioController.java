package net.val.api.autenticacao.controller;

import jakarta.validation.Valid;
import net.val.api.autenticacao.dtos.usuarioDto.DadosCadastroUsuario;
import net.val.api.autenticacao.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarUsuario(@Valid @RequestBody DadosCadastroUsuario dadosCadastroUsuario) {
        usuarioService.cadastrarUsuario(dadosCadastroUsuario);
        String link = "http://localhost:8080/login";

        return ResponseEntity.ok(String.format("Usuário cadastrado com sucesso! Acesse o link %s para autenticação.", link));
    }
}
