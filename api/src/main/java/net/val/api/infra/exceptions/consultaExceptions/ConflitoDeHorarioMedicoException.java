package net.val.api.infra.exceptions.consultaExceptions;

import java.time.LocalDateTime;

public class ConflitoDeHorarioMedicoException extends RuntimeException {
    public ConflitoDeHorarioMedicoException(LocalDateTime dataConsulta, Long medicoId) {
        super(String.format(
                "Não foi possível agendar uma consulta para o horário: %02d:%02d. Médico com ID %d já possui compromisso nesse horário.",
                dataConsulta.getHour(),dataConsulta.getMinute(), medicoId));
    }
}