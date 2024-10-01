package net.val.api.consulta.service.agendarConsulta;

import net.val.api.consulta.enums.StatusConsulta;
import net.val.api.infra.exceptions.consultaExceptions.AntecedenciaInsuficienteException;
import net.val.api.infra.exceptions.consultaExceptions.ConflitoDeHorarioMedicoException;
import net.val.api.infra.exceptions.consultaExceptions.ConflitoDeHorarioPacienteException;
import net.val.api.infra.exceptions.consultaExceptions.ConsultaNaoEncontrada;
import net.val.api.consulta.entity.Consulta;
import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.consulta.dtos.DadosDetalhamentoConsulta;
import net.val.api.infra.exceptions.consultaExceptions.*;
import net.val.api.infra.exceptions.especialidadeExceptions.EspecialidadeNulaException;
import net.val.api.infra.exceptions.medicoExceptions.MedicoInativoException;
import net.val.api.infra.exceptions.medicoExceptions.MedicoNaoEncontradoException;
import net.val.api.infra.exceptions.pacienteExceptions.PacienteNaoEncontradoException;
import net.val.api.consulta.repository.ConsultaRepository;
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

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class ConsultaService {
    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public ConsultaService(@Lazy ConsultaRepository consultaRepository,
                           @Lazy MedicoRepository medicoRepository,
                           @Lazy PacienteRepository pacienteRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public Consulta agendarConsulta(DadosAgendamentoConsulta agendamentoConsulta) {

        // Buscar o paciente
        Paciente paciente = pacienteRepository.findById(agendamentoConsulta.pacienteId())
                .orElseThrow(() -> new PacienteNaoEncontradoException(agendamentoConsulta.pacienteId()));

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


    private void verificarConflitoDeHorario(Long pacienteId, Long medicoId, LocalDateTime dataConsulta) {
        // Definir o início e o fim do intervalo de 60 minutos
        LocalDateTime inicioConsulta = dataConsulta.minusMinutes(59);
        LocalDateTime fimConsulta = dataConsulta.plusMinutes(59);

        // Verificar se há conflito de horário para o médico
        if (consultaRepository.existsByMedicoIdAndDataConsultaBetween(medicoId, inicioConsulta, fimConsulta)) {
            throw new ConflitoDeHorarioMedicoException(dataConsulta, medicoId);
        }

        // Verificar se há conflito de horário para o paciente
        if (consultaRepository.existsByPacienteIdAndDataConsultaBetween(pacienteId, inicioConsulta, fimConsulta)) {
            throw new ConflitoDeHorarioPacienteException(dataConsulta);
        }

        // Verificar se o paciente já tem outra consulta no mesmo dia com o mesmo médico.
        if (consultaRepository.existsByPacienteIdAndMedicoIdAndDataConsulta(pacienteId, medicoId, inicioConsulta,fimConsulta)) {
            throw new PacienteComConsultaDuplicadaException(pacienteId, dataConsulta);
        }
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
