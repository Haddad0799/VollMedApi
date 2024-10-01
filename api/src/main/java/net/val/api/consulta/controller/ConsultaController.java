package net.val.api.consulta.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import net.val.api.consulta.entity.Consulta;
import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.consulta.dtos.DadosDetalhamentoConsulta;
import net.val.api.consulta.service.agendarConsulta.ConsultaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@RestController
@RequestMapping("consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping("/agendar")
    public ResponseEntity<DadosDetalhamentoConsulta> agendar(@RequestBody @Valid DadosAgendamentoConsulta dadosAgendamentoConsulta, UriComponentsBuilder builder) {
        Consulta consulta = consultaService.agendarConsulta(dadosAgendamentoConsulta);

        var uri = builder.path("/consultas/{id}").buildAndExpand(consulta.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoConsulta(consulta, consulta.getMedico(),consulta.getPaciente()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        consultaService.CancelarConsulta(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoConsulta>> listarConsultas(Pageable pageable) {
        return ResponseEntity.ok(consultaService.listarConsultas(pageable));
    }

}
