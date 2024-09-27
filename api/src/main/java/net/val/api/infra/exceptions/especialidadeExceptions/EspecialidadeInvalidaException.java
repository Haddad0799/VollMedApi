package net.val.api.infra.exceptions.especialidadeExceptions;

public class EspecialidadeInvalidaException extends RuntimeException{
    public EspecialidadeInvalidaException(String especialidade) {
        super("Nenhuma especialidade encontrada para: " + especialidade);
    }
}
