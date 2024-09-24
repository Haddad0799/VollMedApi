package net.val.api.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import net.val.api.domain.Consulta;
import net.val.api.dtos.consultaDto.DadosAgendamentoConsulta;
import net.val.api.dtos.consultaDto.DadosDetalhamentoConsulta;
import net.val.api.service.ConsultaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
