package net.val.api.infra.exceptions.tokenExceptions;

public class TokenNotProvidedException extends RuntimeException {
    public TokenNotProvidedException() {
        super("Token não enviado no cabeçalho!");
    }
}
