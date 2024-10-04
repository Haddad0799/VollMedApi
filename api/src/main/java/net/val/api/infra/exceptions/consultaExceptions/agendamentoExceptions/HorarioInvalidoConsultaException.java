package net.val.api.infra.exceptions.consultaExceptions.agendamentoExceptions;

public class HorarioInvalidoConsultaException extends RuntimeException {
    public HorarioInvalidoConsultaException() {
        super("O dia ou horário são inválidos! Consultas de segunda a sábado das 8:00 ás 19:00");
    }
}
