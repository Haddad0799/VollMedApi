package net.val.api.infra.exceptions.tokenExceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Token inválido ou expirado!");
    }
}
