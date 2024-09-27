package net.val.api.domain;

import net.val.api.infra.exceptions.especialidadeExceptions.EspecialidadeInvalidaException;

public enum Especialidade {
    ORTOPEDIA("ortopedia"),
    CARDIOLOGIA("cardiologia"),
    GINECOLOGIA("ginecologia"),
    DERMATOLOGIA("dermatologia");

    Especialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    private final String especialidade;

    public static Especialidade fromEspecialidade(String text) {
        for (Especialidade e : Especialidade.values()) {
            if (e.especialidade.equalsIgnoreCase(text)) {
                return e;
            }
        }
        throw new EspecialidadeInvalidaException(text);
    }
}
