package net.val.api.dtos.consultaDto;

import net.val.api.domain.Consulta;
import net.val.api.domain.Especialidade;
import net.val.api.domain.Medico;
import net.val.api.domain.Paciente;
import net.val.api.dtos.medicoDto.DadosConsultaMedico;
import net.val.api.dtos.pacienteDto.DadosConsultaPaciente;

import java.time.LocalDateTime;

public record DadosDetalhamentoConsulta(
        Long idConsulta,
        DadosConsultaMedico medico,
        DadosConsultaPaciente paciente,
        LocalDateTime dataConsulta,
        Especialidade especialidadeMedica
) {
    public DadosDetalhamentoConsulta (Consulta consulta, Medico medico, Paciente paciente){
        this(consulta.getId(), new DadosConsultaMedico(medico),new DadosConsultaPaciente(paciente),consulta.getDataConsulta(),consulta.getEspecialidadeMedica());
    }
}
