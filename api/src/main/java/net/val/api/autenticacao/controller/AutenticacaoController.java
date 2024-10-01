package net.val.api.autenticacao.controller;

import jakarta.validation.Valid;
import net.val.api.autenticacao.dtos.autenticacaoDto.DadosAutenticacao;
import net.val.api.autenticacao.dtos.tokenDto.DadosTokenJwt;
import net.val.api.autenticacao.service.AutenticacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping
    public ResponseEntity<DadosTokenJwt> efetuarLogin(@RequestBody @Valid DadosAutenticacao autenticacao){
       var token = autenticacaoService.efetuarLogin(autenticacao);

        return ResponseEntity.ok(token);
    }
}
