package net.val.api.dtos.service;

import net.val.api.domain.*;
import net.val.api.dtos.consultaDto.DadosAgendamentoConsulta;
import net.val.api.dtos.consultaDto.DadosDetalhamentoConsulta;
import net.val.api.infra.exceptions.consultaExceptions.*;
import net.val.api.infra.exceptions.especialidadeExceptions.EspecialidadeNulaException;
import net.val.api.infra.exceptions.medicoExceptions.MedicoInativoException;
import net.val.api.infra.exceptions.medicoExceptions.MedicoNaoEncontradoException;
import net.val.api.infra.exceptions.pacienteExceptions.PacienteNaoEncontradoException;
import net.val.api.repositorys.ConsultaRepository;
import net.val.api.repositorys.MedicoRepository;
import net.val.api.repositorys.PacienteRepository;
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
        garantirAntecedencia(agendamentoConsulta.dataConsulta());

        // Validar a data/hora da consulta
        LocalDateTime dataConsulta = agendamentoConsulta.dataConsulta();
        if (!dataIsValid(dataConsulta)) {
            throw new HorarioInvalidoConsultaException();
        }

        // Buscar o paciente
        Paciente paciente = pacienteRepository.findById(agendamentoConsulta.pacienteId())
                .orElseThrow(() -> new PacienteNaoEncontradoException(agendamentoConsulta.pacienteId()));

        Medico medico = selecionarMedico(agendamentoConsulta);

        // Verificar conflitos de horário antes de criar a consulta
        verificarConflitoDeHorario(paciente.getId(), medico.getId(), dataConsulta);

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

    private boolean dataIsValid(LocalDateTime data) {
        DayOfWeek diaDaSemana = data.getDayOfWeek();
        LocalTime hora = data.toLocalTime();

        if (diaDaSemana == DayOfWeek.SUNDAY) {
            return false; // Domingo não é um dia válido
        }

        return (diaDaSemana.getValue() >= DayOfWeek.MONDAY.getValue() &&
                diaDaSemana.getValue() <= DayOfWeek.SATURDAY.getValue()) &&
                !hora.isBefore(LocalTime.of(8, 0)) && !hora.isAfter(LocalTime.of(19, 0)); // Horário de funcionamento
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

    private void garantirAntecedencia(LocalDateTime dataConsulta) {
        LocalDateTime agora = LocalDateTime.now();
        long minutosDeAntecedencia = Duration.between(agora, dataConsulta).toMinutes();

        if (minutosDeAntecedencia <= 30) {
            throw new AntecedenciaInsuficienteException();
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
