package net.val.api.consulta.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.Getter;
import net.val.api.consulta.dtos.DadosCancelamentoConsulta;
import net.val.api.consulta.dtos.DadosConsultaCancelada;
import net.val.api.consulta.entity.Consulta;
import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.consulta.dtos.DadosDetalhamentoConsulta;
import net.val.api.consulta.service.agendarConsulta.AgendarConsultaService;
import net.val.api.consulta.service.cancelarConsulta.CancelarConsultaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@RestController
@RequestMapping("consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {

    private final AgendarConsultaService consultaService;

    private final CancelarConsultaService cancelarConsultaService;

    public ConsultaController(AgendarConsultaService consultaService, CancelarConsultaService cancelarConsultaService) {
        this.consultaService = consultaService;
        this.cancelarConsultaService = cancelarConsultaService;
    }

    @PostMapping("/agendar")
    public ResponseEntity<DadosDetalhamentoConsulta> agendar(@RequestBody @Valid DadosAgendamentoConsulta dadosAgendamentoConsulta, UriComponentsBuilder builder) {
        Consulta consulta = consultaService.agendarConsulta(dadosAgendamentoConsulta);

        var uri = builder.path("/consultas/{id}").buildAndExpand(consulta.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoConsulta(consulta, consulta.getMedico(),consulta.getPaciente()));
    }

    @DeleteMapping()
    public ResponseEntity<DadosConsultaCancelada> cancelar(@RequestBody @Valid DadosCancelamentoConsulta dadosCancelamentoConsulta) {
        return ResponseEntity.ok().body(cancelarConsultaService.cancelarConsulta(dadosCancelamentoConsulta));
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoConsulta>> listarConsultas(Pageable pageable) {
        return ResponseEntity.ok(consultaService.listarConsultas(pageable));
    }

}
