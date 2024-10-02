package net.val.api.consulta.service.validacoes;

import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.consulta.repository.ConsultaRepository;
import net.val.api.infra.exceptions.consultaExceptions.ConflitoDeHorarioPacienteException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidarConflitoDeHorarioPaciente implements ValidarAgendamentoConsulta {

    private final ConsultaRepository consultaRepository;

    public ValidarConflitoDeHorarioPaciente(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Override
    public void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        LocalDateTime inicioConsulta = dadosAgendamentoConsulta.dataConsulta().minusMinutes(59);
        LocalDateTime fimConsulta = dadosAgendamentoConsulta.dataConsulta().minusMinutes(59);

        // Verificar se há conflito de horário para o Paciente
        if (consultaRepository.existsByPacienteIdAndDataConsultaBetween(dadosAgendamentoConsulta.pacienteId(), inicioConsulta, fimConsulta)) {
            throw new ConflitoDeHorarioPacienteException(dadosAgendamentoConsulta.dataConsulta());
        }
    }
}
