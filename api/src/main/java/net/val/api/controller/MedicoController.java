package net.val.api.controller;

import jakarta.validation.Valid;
import net.val.api.dtos.medicoDto.DadosAtualizacaoMedico;
import net.val.api.dtos.medicoDto.DadosCadastraisMedico;
import net.val.api.dtos.medicoDto.DadosListagemMedico;
import net.val.api.dtos.medicoDto.DadosMedicosAtualizados;
import net.val.api.model.Medico;
import net.val.api.service.MedicoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarMedico(@RequestBody @Valid DadosCadastraisMedico dadosCadastraisMedico) {
        medicoService.cadastrarMedico(dadosCadastraisMedico);

       return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>>  listarMedicos(Pageable paginacao){
        return ResponseEntity.ok(medicoService.listarMedicos(paginacao));
    }
    @PutMapping
    public ResponseEntity<DadosMedicosAtualizados> atualizarMedico(@RequestBody @Valid DadosAtualizacaoMedico dadosAtualizacaoMedico) {
       Medico medico = medicoService.atualizar(dadosAtualizacaoMedico);

        return ResponseEntity.ok(new DadosMedicosAtualizados(medico));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        medicoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
