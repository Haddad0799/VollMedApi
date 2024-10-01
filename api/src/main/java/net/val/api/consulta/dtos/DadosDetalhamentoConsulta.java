package net.val.api.consulta.dtos;

import net.val.api.consulta.entity.Consulta;
import net.val.api.medico.enums.Especialidade;
import net.val.api.medico.entity.Medico;
import net.val.api.paciente.entity.Paciente;

import java.time.LocalDateTime;

public record DadosDetalhamentoConsulta(
        Long idConsulta,
        Medico medico,
        Paciente paciente,
        LocalDateTime dataConsulta,
        Especialidade especialidadeMedica
) {
    public DadosDetalhamentoConsulta (Consulta consulta, Medico medico, Paciente paciente){
        this(consulta.getId(),medico,paciente,consulta.getDataConsulta(),consulta.getEspecialidadeMedica());
    }

}
