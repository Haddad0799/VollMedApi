package net.val.api.infra.exceptions.consultaExceptions;

public class HorarioInvalidoConsultaException extends RuntimeException {
    public HorarioInvalidoConsultaException() {
        super("O dia ou horário são inválidos! Consultas de segunda a sexta das 8:00 ás 17:00 e sábado das 8:00 ás 11:00.");
    }
}
