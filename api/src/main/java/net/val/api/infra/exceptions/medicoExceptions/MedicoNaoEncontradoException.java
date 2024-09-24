package net.val.api.infra.exceptions.medicoExceptions;

public class MedicoNaoEncontradoException extends RuntimeException {
    public MedicoNaoEncontradoException(Long id) {
        super("Nenhum médico encontrado para o ID fornecido: " + id);
    }

}
