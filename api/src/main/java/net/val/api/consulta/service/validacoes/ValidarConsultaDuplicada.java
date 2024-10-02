package net.val.api.consulta.service.validacoes;

import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.consulta.repository.ConsultaRepository;
import net.val.api.infra.exceptions.consultaExceptions.PacienteComConsultaDuplicadaException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidarConsultaDuplicada implements ValidarAgendamentoConsulta {

    private final ConsultaRepository consultaRepository;

    public ValidarConsultaDuplicada(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Override
    public void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {

        LocalDateTime inicioConsulta = dadosAgendamentoConsulta.dataConsulta().minusMinutes(59);
        LocalDateTime fimConsulta = dadosAgendamentoConsulta.dataConsulta().minusMinutes(59);


        if (consultaRepository.existsByPacienteIdAndMedicoIdAndDataConsulta(dadosAgendamentoConsulta.pacienteId(), dadosAgendamentoConsulta.medicoId(), inicioConsulta, fimConsulta)) {
            throw new PacienteComConsultaDuplicadaException(dadosAgendamentoConsulta.pacienteId(), dadosAgendamentoConsulta.dataConsulta());
        }
    }
}
