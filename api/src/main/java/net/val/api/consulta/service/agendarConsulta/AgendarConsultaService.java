package net.val.api.consulta.service.agendarConsulta;

import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.consulta.dtos.DadosDetalhamentoConsulta;
import net.val.api.consulta.entity.Consulta;
import net.val.api.consulta.enums.StatusConsulta;
import net.val.api.consulta.repository.ConsultaRepository;
import net.val.api.consulta.service.validacoes.ValidarAgendamentoConsulta;
import net.val.api.infra.exceptions.consultaExceptions.ConsultaNaoEncontrada;
import net.val.api.infra.exceptions.especialidadeExceptions.EspecialidadeNulaException;
import net.val.api.infra.exceptions.medicoExceptions.MedicoInativoException;
import net.val.api.infra.exceptions.medicoExceptions.MedicoNaoEncontradoException;
import net.val.api.infra.exceptions.pacienteExceptions.PacienteNaoEncontradoException;
import net.val.api.medico.entity.Medico;
import net.val.api.medico.enums.Especialidade;
import net.val.api.medico.repository.MedicoRepository;
import net.val.api.paciente.entity.Paciente;
import net.val.api.paciente.repository.PacienteRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AgendarConsultaService {
    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final List<ValidarAgendamentoConsulta> validacoesAgendamentoConsulta;

    public AgendarConsultaService(@Lazy ConsultaRepository consultaRepository,
                                  @Lazy MedicoRepository medicoRepository,
                                  @Lazy PacienteRepository pacienteRepository, List<ValidarAgendamentoConsulta> validacoesAgendamentoConsulta) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.validacoesAgendamentoConsulta = validacoesAgendamentoConsulta;
    }

    @Transactional
    public Consulta agendarConsulta(DadosAgendamentoConsulta agendamentoConsulta) {

        if(!pacienteRepository.existsById(agendamentoConsulta.pacienteId())) {
            throw new PacienteNaoEncontradoException(agendamentoConsulta.pacienteId());
        }

        if(agendamentoConsulta.medicoId() != null && !medicoRepository.existsById(agendamentoConsulta.medicoId())) {
            throw new MedicoNaoEncontradoException(agendamentoConsulta.medicoId());
        }

        //Validações de consulta.
        validacoesAgendamentoConsulta.forEach(v -> v.validar(agendamentoConsulta));

        Paciente paciente = pacienteRepository.getReferenceById(agendamentoConsulta.pacienteId());

        Medico medico = selecionarMedico(agendamentoConsulta);

        // Criar e salvar a consulta
        Consulta consulta = new Consulta(agendamentoConsulta, medico, paciente);

        return consultaRepository.save(consulta);
    }

    private Medico selecionarMedico(DadosAgendamentoConsulta agendamentoConsulta) {
        if (agendamentoConsulta.medicoId() == null) {
            if (agendamentoConsulta.especialidadeMedica() == null){
                throw new EspecialidadeNulaException();
            }

            Optional<Medico> medicoAleatorio = medicoRepository.medicoAletorio(Especialidade.fromEspecialidade(agendamentoConsulta.especialidadeMedica()));

            return medicoAleatorio.orElseThrow(() -> new MedicoNaoEncontradoException(Especialidade.fromEspecialidade(agendamentoConsulta.especialidadeMedica())));
        }

        Medico medico = medicoRepository.findById(agendamentoConsulta.medicoId())
                .orElseThrow(() -> new MedicoNaoEncontradoException(agendamentoConsulta.medicoId()));

        if (!medico.isAtivo()) {
            throw new MedicoInativoException(medico.getId());
        }

        return medico;
    }

    @Transactional
    public void CancelarConsulta(Long consultaId) {
        Optional<Consulta> consultaOptional = consultaRepository.findById(consultaId);

        if(consultaOptional.isEmpty()) {
            throw new ConsultaNaoEncontrada(consultaId);
        }

        Consulta consulta = consultaOptional.get();

        consulta.setStatus(StatusConsulta.CANCELADA);

        consultaRepository.save(consulta);

    }
    public Page<DadosDetalhamentoConsulta> listarConsultas(Pageable pageable) {
        return consultaRepository.findAllAgendadas(pageable).map(consulta -> new DadosDetalhamentoConsulta(consulta.getId(),consulta.getMedico(),consulta.getPaciente(),consulta.getDataConsulta(),consulta.getEspecialidadeMedica()));

    }

}
