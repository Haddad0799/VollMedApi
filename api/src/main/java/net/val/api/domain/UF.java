package net.val.api.domain;

public enum UF {
    AC("AC"),
    AL("AL"),
    AP("AP"),
    AM("AM"),
    BA("BA"),
    CE("CE"),
    DF("DF"),
    ES("ES"),
    GO("GO"),
    MA("MA"),
    MT("MT"),
    MS("MS"),
    MG("MG"),
    PA("PA"),
    PB("PB"),
    PR("PR"),
    PE("PE"),
    PI("PI"),
    RJ("RJ"),
    RN("RN"),
    RS("RS"),
    RO("RO"),
    RR("RR"),
    SC("SC"),
    SP("SP"),
    SE("SE"),
    TO("TO");

    private final String ufString;

    UF(String ufString) {
        this.ufString = ufString;
    }

    public static UF fromString(String text) {
        for (UF uf : UF.values()) {
            if (uf.ufString.equalsIgnoreCase(text)) {
                return uf;
            }
        }
        throw new IllegalArgumentException("Nenhuma UF encontrada: " + text);
    }
}
