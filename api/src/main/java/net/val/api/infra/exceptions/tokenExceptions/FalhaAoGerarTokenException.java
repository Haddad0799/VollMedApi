package net.val.api.infra.exceptions.tokenExceptions;

public class FalhaAoGerarTokenException extends RuntimeException {
    public FalhaAoGerarTokenException() {
        super("Houve um problema na criação do token. Tente novamente.");
    }
}
