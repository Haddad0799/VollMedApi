package net.val.api.infra.exceptions.consultaExceptions.cancelamentoExceptions;

public class CancelamentoAntecipadoException extends RuntimeException{

    public CancelamentoAntecipadoException() {
        super("Não é possível cancelar uma consulta com o prazo superior a 24 horas de antecedência.");
    }
}
