package net.val.api.controller;

import jakarta.validation.Valid;
import net.val.api.dtos.pacienteDto.DadosAtualizacaoPaciente;
import net.val.api.dtos.pacienteDto.DadosCadastraisPaciente;
import net.val.api.dtos.pacienteDto.DadosDetalhamentoPaciente;
import net.val.api.dtos.pacienteDto.DadosListagemPacientes;
import net.val.api.domain.Paciente;
import net.val.api.service.PacienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;


    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoPaciente> cadastrarPaciente(@RequestBody @Valid DadosCadastraisPaciente dadosCadastraisPaciente, UriComponentsBuilder uriBuilder){
        Paciente paciente = pacienteService.CadastrarPaciente(dadosCadastraisPaciente);

        var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPacientes>>  listarPacientes(Pageable paginacao) {
        return ResponseEntity.ok(pacienteService.listarPacientes(paginacao));
    }

    @PutMapping
    public ResponseEntity<DadosDetalhamentoPaciente> atualizarPaciente(@RequestBody @Valid DadosAtualizacaoPaciente dadosAtualizacaoPaciente) {
        Paciente paciente = pacienteService.atualizarPaciente(dadosAtualizacaoPaciente);

        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPaciente(@PathVariable Long id) {
        pacienteService.excluirPaciente(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{pacienteId}")
    public ResponseEntity<DadosDetalhamentoPaciente> detalharPaciente(@PathVariable Long pacienteId){
        return ResponseEntity.ok(pacienteService.detalharPaciente(pacienteId));
    }

}
