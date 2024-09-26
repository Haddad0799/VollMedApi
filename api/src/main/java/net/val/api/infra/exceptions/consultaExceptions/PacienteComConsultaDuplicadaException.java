package net.val.api.infra.exceptions.consultaExceptions;

import java.time.LocalDateTime;

public class PacienteComConsultaDuplicadaException extends RuntimeException {
    public PacienteComConsultaDuplicadaException(Long pacienteId, LocalDateTime dataConsulta) {
        super(String.format(
                "Paciente com ID %d já possui uma consulta para essa data %d/%d/%d. Verifique o horário corretamente.",
                pacienteId, dataConsulta.getDayOfMonth(), dataConsulta.getMonthValue(), dataConsulta.getYear()));
    }
}

