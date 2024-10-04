package net.val.api.infra.exceptions.consultaExceptions.cancelamentoExceptions;

public class MotivoCancelamentoIncorretoException extends RuntimeException{
    public MotivoCancelamentoIncorretoException(String descricao) {
        super("Motivo para o cancelamento inválido: " + descricao);
    }
}
