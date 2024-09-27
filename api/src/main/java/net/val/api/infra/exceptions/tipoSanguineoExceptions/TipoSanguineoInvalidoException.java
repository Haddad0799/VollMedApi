package net.val.api.infra.exceptions.tipoSanguineoExceptions;

public class TipoSanguineoInvalidoException extends RuntimeException {
    public TipoSanguineoInvalidoException(String tipo) {
        super("Tipo sanguíneo inválido para o tipo: " + tipo);
    }
}
