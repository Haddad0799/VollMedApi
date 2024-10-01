package net.val.api.infra.exceptions.medicoExceptions;

import net.val.api.medico.enums.Especialidade;

public class MedicoNaoEncontradoException extends RuntimeException {
    public MedicoNaoEncontradoException(Long id) {
        super("Nenhum médico encontrado para o ID fornecido: " + id);
    }

    public MedicoNaoEncontradoException(Especialidade especialidade) {
        super("Nenhum médico encontrado para a especialidade: " + especialidade);
    }

}
