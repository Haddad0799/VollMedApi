package net.val.api.medico.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import net.val.api.medico.dtos.DadosAtualizacaoMedico;
import net.val.api.medico.dtos.DadosCadastraisMedico;
import net.val.api.medico.dtos.DadosListagemMedico;
import net.val.api.medico.dtos.DadosDetalhamentoMedico;
import net.val.api.medico.entity.Medico;
import net.val.api.medico.service.MedicoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoMedico> cadastrarMedico(@RequestBody @Valid DadosCadastraisMedico dadosCadastraisMedico, UriComponentsBuilder builder) {
       Medico medico =  medicoService.cadastrarMedico(dadosCadastraisMedico);
       //Devolver no cabeçalho da requisição a uri do local onde o cadastro foi realizado.
       var uri = builder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

       return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>>  listarMedicos(Pageable paginacao){
        return ResponseEntity.ok(medicoService.listarMedicos(paginacao));
    }
    @PutMapping
    public ResponseEntity<DadosDetalhamentoMedico> atualizarMedico(@RequestBody @Valid DadosAtualizacaoMedico dadosAtualizacaoMedico) {
       Medico medico = medicoService.atualizar(dadosAtualizacaoMedico);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        medicoService.excluir(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{medicoId}")
    public ResponseEntity<DadosDetalhamentoMedico> detalhamentoMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(medicoService.detalharMedico(medicoId));
    }
}
