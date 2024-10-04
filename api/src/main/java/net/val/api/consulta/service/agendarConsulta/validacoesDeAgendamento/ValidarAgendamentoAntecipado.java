package net.val.api.consulta.service.agendarConsulta.validacoesDeAgendamento;

import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.infra.exceptions.consultaExceptions.agendamentoExceptions.AntecedenciaInsuficienteException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidarAgendamentoAntecipado implements ValidarAgendamentoConsulta {
    @Override
    public void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        LocalDateTime agora = LocalDateTime.now();
        long minutosDeAntecedencia = Duration.between(agora, dadosAgendamentoConsulta.dataConsulta()).toMinutes();

        if (minutosDeAntecedencia <= 30) {
            throw new AntecedenciaInsuficienteException();
        }
    }
}
