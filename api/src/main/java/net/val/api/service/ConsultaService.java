package net.val.api.service;

import net.val.api.domain.Consulta;
import net.val.api.domain.Medico;
import net.val.api.domain.Paciente;
import net.val.api.dtos.consultaDto.DadosAgendamentoConsulta;
import net.val.api.infra.exceptions.consultaExceptions.ConflitoDeHorarioMedicoException;
import net.val.api.infra.exceptions.consultaExceptions.ConflitoDeHorarioPacienteException;
import net.val.api.infra.exceptions.consultaExceptions.HorarioInvalidoConsultaException;
import net.val.api.infra.exceptions.consultaExceptions.PacienteComConsultaDuplicadaException;
import net.val.api.infra.exceptions.medicoExceptions.MedicoInativoException;
import net.val.api.infra.exceptions.medicoExceptions.MedicoNaoEncontradoException;
import net.val.api.infra.exceptions.pacienteExceptions.PacienteNaoEncontradoException;
import net.val.api.repositorys.ConsultaRepository;
import net.val.api.repositorys.MedicoRepository;
import net.val.api.repositorys.PacienteRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
        // Buscar o médico
        Medico medico = medicoRepository.findById(agendamentoConsulta.medicoId())
                .orElseThrow(() -> new MedicoNaoEncontradoException(agendamentoConsulta.medicoId()));

        // Buscar o paciente
        Paciente paciente = pacienteRepository.findById(agendamentoConsulta.pacienteId())
                .orElseThrow(() -> new PacienteNaoEncontradoException(agendamentoConsulta.pacienteId()));

        // Validar o estado do médico
        if (!medico.isAtivo()) {
            throw new MedicoInativoException(medico.getId());
        }

        // Validar a data/hora da consulta
        LocalDateTime dataConsulta = agendamentoConsulta.dataConsulta();
        if (!dataIsValid(dataConsulta)) {
            throw new HorarioInvalidoConsultaException();
        }

        // Verificar conflitos de horário
         verificarConflitoDeHorario(paciente.getId(), medico.getId(), dataConsulta);

        // Criar e salvar a consulta
        Consulta consulta = new Consulta(agendamentoConsulta, medico, paciente);
        consultaRepository.save(consulta);

        return consulta;
    }

    private boolean dataIsValid(LocalDateTime data) {
        DayOfWeek diaDaSemana = data.getDayOfWeek();
        LocalTime hora = data.toLocalTime();

        if (diaDaSemana == DayOfWeek.SUNDAY) {
            return false;
        }

        if (diaDaSemana.getValue() >= DayOfWeek.MONDAY.getValue()
                && diaDaSemana.getValue() <= DayOfWeek.FRIDAY.getValue()) {
            return  !hora.isBefore(LocalTime.of(8, 0)) && !hora.isAfter(LocalTime.of(17, 0));
        }

        if (diaDaSemana.getValue() == DayOfWeek.SATURDAY.getValue()) {
            return !hora.isBefore(LocalTime.of(8, 0)) && !hora.isAfter(LocalTime.of(11, 0));
        }
        return false;
    }

    private void verificarConflitoDeHorario(Long pacienteId, Long medicoId, LocalDateTime dataConsulta) {
        // Definir o início e o fim do intervalo de 30 minutos
        LocalDateTime inicioConsulta = dataConsulta.minusMinutes(29);
        LocalDateTime fimConsulta = dataConsulta.plusMinutes(29);

        // Verificar se há conflito de horário para o médico
        if (consultaRepository.existsByMedicoIdAndDataConsultaBetween(medicoId, inicioConsulta, fimConsulta)) {
            throw new ConflitoDeHorarioMedicoException(dataConsulta, medicoId);
        }

        // Verificar se há conflito de horário para o paciente
        if (consultaRepository.existsByPacienteIdAndDataConsultaBetween(pacienteId, inicioConsulta, fimConsulta)) {
            throw new ConflitoDeHorarioPacienteException(dataConsulta);
        }

        // Verificar se o paciente já tem outra consulta no mesmo dia com o mesmo médico
        if (consultaRepository.existsByPacienteIdAndMedicoIdAndDataConsulta(pacienteId, medicoId, dataConsulta.toLocalDate())) {
            throw new PacienteComConsultaDuplicadaException(pacienteId,dataConsulta);
        }
    }



}
