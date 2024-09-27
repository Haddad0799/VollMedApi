package net.val.api.infra.exceptions.consultaExceptions;

import java.time.LocalDateTime;

public class DataInvalidaException extends RuntimeException{
    public DataInvalidaException() {
        super("Data inválida para agendamento de consulta. Verifique o dia e mês corretamente");
    }
}
