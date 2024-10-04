package net.val.api.infra.exceptions.consultaExceptions.cancelamentoExceptions;

public class ConsultaJaCanceladaException extends RuntimeException{
    public ConsultaJaCanceladaException(Long consultaId) {
        super("Consulta de ID " + consultaId + " já foi cancelada.");
    }
}
