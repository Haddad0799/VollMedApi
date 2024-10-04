package net.val.api.infra.exceptions.consultaExceptions.agendamentoExceptions;

import java.time.LocalDateTime;

public class ConflitoDeHorarioPacienteException extends RuntimeException{

        public ConflitoDeHorarioPacienteException(LocalDateTime dataConsulta) {
            super(String.format("Não foi possível agendar uma consulta para o horário: %02d:%02d. Horário já agendado para outro paciente.",
                    dataConsulta.getHour(), dataConsulta.getMinute()));
        }
    }

