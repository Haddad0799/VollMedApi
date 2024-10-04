package net.val.api.consulta.dtos;

import net.val.api.consulta.entity.Consulta;
import net.val.api.consulta.enums.MotivoCancelamento;

public record DadosConsultaCancelada(
        Consulta consulta,
        MotivoCancelamento motivoCancelamento


) {
    public DadosConsultaCancelada(Consulta consulta, MotivoCancelamento motivoCancelamento) {
        this.consulta = consulta;
        this.motivoCancelamento = motivoCancelamento;
    }
}
