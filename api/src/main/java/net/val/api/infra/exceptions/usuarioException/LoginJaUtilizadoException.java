package net.val.api.infra.exceptions.usuarioException;

public class LoginJaUtilizadoException extends RuntimeException{
    public LoginJaUtilizadoException(String login) {
        super("O email digiado já está em uso: " + login);
    }
}
