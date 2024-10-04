package net.val.api.consulta.dtos;

import net.val.api.consulta.entity.Consulta;
import net.val.api.consulta.enums.MotivoCancelamento;

public record DadosConsultaCancelada(
       DadosDetalhamentoConsulta dadosDetalhamentoConsulta,
        MotivoCancelamento motivoCancelamento


) {
    public DadosConsultaCancelada(DadosDetalhamentoConsulta dadosDetalhamentoConsulta, MotivoCancelamento motivoCancelamento) {
        this.dadosDetalhamentoConsulta = dadosDetalhamentoConsulta;
        this.motivoCancelamento = motivoCancelamento;
    }
}
