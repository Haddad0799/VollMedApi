package net.val.api.endereco.enums;

import net.val.api.infra.exceptions.ufExceptions.UfInvalidaException;

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

    public static UF fromUf(String text) {
        for (UF uf : UF.values()) {
            if (uf.ufString.equalsIgnoreCase(text)) {
                return uf;
            }
        }
        throw new UfInvalidaException(text);
    }
}
