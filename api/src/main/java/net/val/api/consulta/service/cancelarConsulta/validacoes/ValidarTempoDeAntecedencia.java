package net.val.api.consulta.service.cancelarConsulta.validacoes;

import net.val.api.consulta.dtos.DadosCancelamentoConsulta;
import net.val.api.consulta.entity.Consulta;
import net.val.api.consulta.repository.ConsultaRepository;
import net.val.api.infra.exceptions.consultaExceptions.cancelamentoExceptions.CancelamentoAntecipadoException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidarTempoDeAntecedencia implements ValidarCancelamentoConsulta{

    private final ConsultaRepository consultaRepository;

    public ValidarTempoDeAntecedencia(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Override
    public void validar(DadosCancelamentoConsulta dadosCancelamentoConsulta) {

        Consulta consulta = consultaRepository.getReferenceById(dadosCancelamentoConsulta.consultaId());

        LocalDateTime dataCancelamento = LocalDateTime.now();


        if(dataCancelamento.isAfter(consulta.getDataConsulta().minusHours(24))) {
            throw new CancelamentoAntecipadoException();
        }

    }
}
