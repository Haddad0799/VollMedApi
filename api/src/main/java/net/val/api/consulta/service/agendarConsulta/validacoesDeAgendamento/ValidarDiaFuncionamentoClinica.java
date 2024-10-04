package net.val.api.consulta.service.agendarConsulta.validacoesDeAgendamento;

import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.infra.exceptions.consultaExceptions.agendamentoExceptions.HorarioInvalidoConsultaException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class ValidarDiaFuncionamentoClinica implements ValidarAgendamentoConsulta {
    @Override
    public void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta) {
        // Validar a data/hora da consulta
        LocalDateTime dataConsulta = dadosAgendamentoConsulta.dataConsulta();
        if (!dataIsValid(dataConsulta)) {
            throw new HorarioInvalidoConsultaException();
        }
    }

    private boolean dataIsValid(LocalDateTime data) {
        DayOfWeek diaDaSemana = data.getDayOfWeek();
        LocalTime hora = data.toLocalTime();

        if (diaDaSemana == DayOfWeek.SUNDAY) {
            return false; // Domingo não é um dia válido
        }

        return (diaDaSemana.getValue() >= DayOfWeek.MONDAY.getValue() &&
                diaDaSemana.getValue() <= DayOfWeek.SATURDAY.getValue()) &&
                !hora.isBefore(LocalTime.of(8, 0)) && !hora.isAfter(LocalTime.of(18, 0)); // Horário de funcionamento
    }
}


