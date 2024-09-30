package net.val.api.infra.exceptions.especialidadeExceptions;

public class EspecialidadeNulaException extends RuntimeException{
    public EspecialidadeNulaException() {
        super("A especilaidade médica é obrigatória se o campo com o ID do médico estiver nulo!");
    }
}
