package net.val.api.consulta.service.agendarConsulta.validacoesDeAgendamento;

import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

@Component
public interface ValidarAgendamentoConsulta {

    void validar(DadosAgendamentoConsulta dadosAgendamentoConsulta);
}
