package net.val.api.controller;

import jakarta.validation.Valid;
import net.val.api.dtos.medicoDto.DadosAtualizacaoMedico;
import net.val.api.dtos.medicoDto.DadosCadastraisMedico;
import net.val.api.dtos.medicoDto.DadosListagemMedico;
import net.val.api.service.MedicoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public void cadastrarMedico(@RequestBody @Valid DadosCadastraisMedico dadosCadastraisMedico) {
        medicoService.cadastrarMedico(dadosCadastraisMedico);
    }

    @GetMapping
    public Page<DadosListagemMedico> listarMedicos(Pageable paginacao){
        return medicoService.listarMedicos(paginacao);
    }
    @PutMapping
    public void atualizarMedico(@RequestBody @Valid DadosAtualizacaoMedico dadosAtualizacaoMedico) {
        medicoService.atualizar(dadosAtualizacaoMedico);
    }
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id){
        medicoService.excluir(id);
    }
}
