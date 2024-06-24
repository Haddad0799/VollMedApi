package net.val.api.controller;

import jakarta.validation.Valid;
import net.val.api.dtos.pacienteDto.DadosCadastraisPaciente;
import net.val.api.dtos.pacienteDto.DadosListagemPacientes;
import net.val.api.service.PacienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;


    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public void cadastrarPaciente(@RequestBody @Valid DadosCadastraisPaciente dadosCadastraisPaciente){
        pacienteService.CadastrarPaciente(dadosCadastraisPaciente);
    }

    @GetMapping
    public Page<DadosListagemPacientes> listarPacientes(Pageable paginacao) {
        return pacienteService.listarPacientes(paginacao);
    }
}
