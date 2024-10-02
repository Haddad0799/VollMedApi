package net.val.api.consulta.service.validacoes;

import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.consulta.repository.ConsultaRepository;
import net.val.api.infra.exceptions.consultaExceptions.ConflitoDeHorarioMedicoException;
import net.val.api.infra.exceptions.consultaExceptions.ConflitoDeHorarioPacienteException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidarConflitoDeHorario implements ValidarAgendamentoConsulta {

    private final ConsultaRepository consultaRepository;

    public ValidarConflitoDeHorario(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Override
    public void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        LocalDateTime inicioConsulta = dadosAgendamentoConsulta.dataConsulta();
        LocalDateTime fimConsulta = inicioConsulta.plusMinutes(59);

        // Verifica se há alguma consulta do médico no intervalo de uma hora antes ou uma hora depois
        LocalDateTime intervaloInicio = inicioConsulta.minusMinutes(59);

        // Verificar se há conflito de horário para o médico
        if (consultaRepository.existsByMedicoIdAndDataConsultaBetween(dadosAgendamentoConsulta.medicoId(), intervaloInicio, fimConsulta)) {
            throw new ConflitoDeHorarioMedicoException(dadosAgendamentoConsulta.dataConsulta(), dadosAgendamentoConsulta.medicoId());
        }

        if (consultaRepository.existsByPacienteIdAndDataConsultaBetween(dadosAgendamentoConsulta.pacienteId(), intervaloInicio, fimConsulta)) {
            throw new ConflitoDeHorarioPacienteException(dadosAgendamentoConsulta.dataConsulta());
        }
    }
}
