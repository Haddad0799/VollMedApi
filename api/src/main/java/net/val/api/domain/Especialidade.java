package net.val.api.domain;

public enum Especialidade {
    ORTOPEDIA("ortopedia"),
    CARDIOLOGIA("cardiologia"),
    GINECOLOGIA("ginecologia"),
    DERMATOLOGIA("dermatologia");

    Especialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    private final String especialidade;

    public static Especialidade fromString(String text) {
        for (Especialidade e : Especialidade.values()) {
            if (e.especialidade.equalsIgnoreCase(text)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Nenhuma especialidade encontrada: " + text);
    }
}
