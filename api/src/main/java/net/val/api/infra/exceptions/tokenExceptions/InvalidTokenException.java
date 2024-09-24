package net.val.api.infra.exceptions.tokenExceptions;

public class InvalidTokenException extends Throwable {
    public InvalidTokenException() {
        super("Token inválido ou expirado!");
    }
}
