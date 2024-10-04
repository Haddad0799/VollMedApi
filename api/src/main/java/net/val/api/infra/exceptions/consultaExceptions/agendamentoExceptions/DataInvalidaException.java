package net.val.api.infra.exceptions.consultaExceptions.agendamentoExceptions;

public class DataInvalidaException extends RuntimeException{
    public DataInvalidaException() {
        super("Data inválida para agendamento de consulta. Verifique o dia e mês corretamente");
    }
}
