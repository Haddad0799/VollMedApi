package net.val.api.consulta.service.cancelarConsulta;

import net.val.api.consulta.dtos.DadosCancelamentoConsulta;
import net.val.api.consulta.dtos.DadosConsultaCancelada;
import net.val.api.consulta.dtos.DadosDetalhamentoConsulta;
import net.val.api.consulta.entity.Consulta;
import net.val.api.consulta.enums.MotivoCancelamento;
import net.val.api.consulta.enums.StatusConsulta;
import net.val.api.consulta.repository.ConsultaRepository;
import net.val.api.consulta.service.cancelarConsulta.validacoes.ValidarCancelamentoConsulta;
import net.val.api.infra.exceptions.consultaExceptions.agendamentoExceptions.ConsultaNaoEncontrada;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CancelarConsultaService {

    private final ConsultaRepository consultaRepository;

    private final List<ValidarCancelamentoConsulta> validarCancelamentoConsulta;

    public CancelarConsultaService(ConsultaRepository consultaRepository, List<ValidarCancelamentoConsulta> validarCancelamentoConsulta) {
        this.consultaRepository = consultaRepository;
        this.validarCancelamentoConsulta = validarCancelamentoConsulta;
    }


    @Transactional
    public DadosConsultaCancelada cancelarConsulta(DadosCancelamentoConsulta dadosCancelamentoConsulta) {

        Optional<Consulta> consultaOptional = consultaRepository.findById(dadosCancelamentoConsulta.consultaId());

        if(consultaOptional.isEmpty()) {
            throw new ConsultaNaoEncontrada(dadosCancelamentoConsulta.consultaId());
        }

        MotivoCancelamento motivoCancelamento = MotivoCancelamento.fromDescricao(dadosCancelamentoConsulta.motivoCancelamento());

        validarCancelamentoConsulta.forEach(v -> v.validar(dadosCancelamentoConsulta));

        Consulta consulta = consultaOptional.get();

        consulta.setStatus(StatusConsulta.CANCELADA);

        DadosDetalhamentoConsulta dadosDetalhamentoConsulta = new DadosDetalhamentoConsulta(consulta,consulta.getMedico(),consulta.getPaciente());

        consultaRepository.save(consulta);

      return new DadosConsultaCancelada(dadosDetalhamentoConsulta,motivoCancelamento);

    }
}
