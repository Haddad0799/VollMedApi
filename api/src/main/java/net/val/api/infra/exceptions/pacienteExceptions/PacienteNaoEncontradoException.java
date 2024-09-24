package net.val.api.infra.exceptions.pacienteExceptions;

public class PacienteNaoEncontradoException extends RuntimeException {
    public PacienteNaoEncontradoException(Long id) {
        super("Nenhum paciente encontrado para o ID fornecido: " + id);
    }
}
