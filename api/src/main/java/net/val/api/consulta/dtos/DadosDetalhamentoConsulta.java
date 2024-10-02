package net.val.api.consulta.dtos;

import net.val.api.consulta.entity.Consulta;
import net.val.api.medico.dtos.DadosConsultaMedico;
import net.val.api.medico.enums.Especialidade;
import net.val.api.medico.entity.Medico;
import net.val.api.paciente.dtos.DadosConsultaPaciente;
import net.val.api.paciente.entity.Paciente;

import java.time.LocalDateTime;

public record DadosDetalhamentoConsulta(
        Long idConsulta,
        DadosConsultaPaciente dadosConsultaPaciente,
        DadosConsultaMedico dadosConsultaMedico,
        LocalDateTime dataConsulta,
        Especialidade especialidadeMedica
) {
    public DadosDetalhamentoConsulta (Consulta consulta, Medico medico, Paciente paciente){
        this(consulta.getId(),
                new DadosConsultaPaciente(paciente),
                new DadosConsultaMedico(medico),
                consulta.getDataConsulta() ,
                consulta.getEspecialidadeMedica());
    }

}
