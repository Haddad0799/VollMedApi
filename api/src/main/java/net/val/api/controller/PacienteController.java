package net.val.api.controller;

import jakarta.validation.Valid;
import net.val.api.dtos.pacienteDto.DadosAtualizacaoPaciente;
import net.val.api.dtos.pacienteDto.DadosCadastraisPaciente;
import net.val.api.dtos.pacienteDto.DadosListagemPacientes;
import net.val.api.dtos.pacienteDto.DadosPacientesAtualizados;
import net.val.api.model.Paciente;
import net.val.api.service.PacienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;


    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarPaciente(@RequestBody @Valid DadosCadastraisPaciente dadosCadastraisPaciente){
        pacienteService.CadastrarPaciente(dadosCadastraisPaciente);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPacientes>>  listarPacientes(Pageable paginacao) {
        return ResponseEntity.ok(pacienteService.listarPacientes(paginacao));
    }

    @PutMapping
    public ResponseEntity<DadosPacientesAtualizados> atualizarPaciente(@RequestBody @Valid DadosAtualizacaoPaciente dadosAtualizacaoPaciente) {
        Paciente paciente = pacienteService.atualizarPaciente(dadosAtualizacaoPaciente);

        return ResponseEntity.ok(new DadosPacientesAtualizados(paciente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPaciente(@PathVariable Long id) {
        pacienteService.excluirPaciente(id);

        return ResponseEntity.noContent().build();
    }

}
