package net.val.api.consulta.service.cancelarConsulta.validacoes;

import net.val.api.consulta.dtos.DadosCancelamentoConsulta;
import net.val.api.consulta.entity.Consulta;
import net.val.api.consulta.enums.StatusConsulta;
import net.val.api.consulta.repository.ConsultaRepository;
import net.val.api.infra.exceptions.consultaExceptions.cancelamentoExceptions.ConsultaJaCanceladaException;
import org.springframework.stereotype.Component;

@Component
public class ValidarConsultaJaCancelada implements ValidarCancelamentoConsulta {

    private final ConsultaRepository consultaRepository;

    public ValidarConsultaJaCancelada(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Override
    public void validar(DadosCancelamentoConsulta dadosCancelamentoConsulta) {
        Consulta consulta = consultaRepository.getReferenceById(dadosCancelamentoConsulta.consultaId());

        if (consulta.getStatus().equals(StatusConsulta.CANCELADA)) {
            throw new ConsultaJaCanceladaException(dadosCancelamentoConsulta.consultaId());
        }
    }
}
