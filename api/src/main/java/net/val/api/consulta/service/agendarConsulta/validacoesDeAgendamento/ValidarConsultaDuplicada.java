package net.val.api.consulta.service.agendarConsulta.validacoesDeAgendamento;

import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.consulta.repository.ConsultaRepository;
import net.val.api.infra.exceptions.consultaExceptions.agendamentoExceptions.PacienteComConsultaDuplicadaException;
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

        LocalDateTime dataconsulta = dadosAgendamentoConsulta.dataConsulta();

        if (consultaRepository.existsByPacienteIdAndMedicoIdAndDataConsultaNoMesmoDia(dadosAgendamentoConsulta.pacienteId(), dadosAgendamentoConsulta.medicoId(),dataconsulta)) {
            throw new PacienteComConsultaDuplicadaException(dadosAgendamentoConsulta.pacienteId(), dadosAgendamentoConsulta.dataConsulta());
        }
    }
}
