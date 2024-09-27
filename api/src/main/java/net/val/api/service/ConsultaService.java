package net.val.api.service;

import net.val.api.domain.Consulta;
import net.val.api.domain.Especialidade;
import net.val.api.domain.Medico;
import net.val.api.domain.Paciente;
import net.val.api.dtos.consultaDto.DadosAgendamentoConsulta;
import net.val.api.infra.exceptions.consultaExceptions.*;
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

        Medico medico;
        Paciente paciente;

        garantirAntecedencia(agendamentoConsulta.dataConsulta());

        // Validar a data/hora da consulta
        LocalDateTime dataConsulta = agendamentoConsulta.dataConsulta();
        if (!dataIsValid(dataConsulta)) {
            throw new HorarioInvalidoConsultaException();
        }

        // Buscar o paciente
        paciente = pacienteRepository.findById(agendamentoConsulta.pacienteId())
                .orElseThrow(() -> new PacienteNaoEncontradoException(agendamentoConsulta.pacienteId()));


        if(agendamentoConsulta.medicoId().describeConstable().isEmpty()){

              Optional<Medico> medicoAleatorio = medicoRepository.medicoAletorio(Especialidade.fromEspecialidade(agendamentoConsulta.especialidadeMedica()));

              if(medicoAleatorio.isPresent()) {
                  medico = medicoAleatorio.get();
                  Consulta consulta = new Consulta(agendamentoConsulta, medico, paciente);
                  consultaRepository.save(consulta);

                  // Verificar conflitos de horário
                  verificarConflitoDeHorario(paciente.getId(), medico.getId(), dataConsulta);

                  return consulta;
              }
            }

             medico = medicoRepository.findByIdAndAndEspecialidade(agendamentoConsulta.medicoId(),Especialidade.fromEspecialidade(agendamentoConsulta.especialidadeMedica()))
                    .orElseThrow(() -> new MedicoNaoEncontradoException(agendamentoConsulta.medicoId()));

            // Validar o estado do médico
            if (!medico.isAtivo()) {
                throw new MedicoInativoException(medico.getId());
            }

        // Verificar conflitos de horário
            verificarConflitoDeHorario(paciente.getId(), medico.getId(), dataConsulta);

            // Criar e salvar a consulta enviada com um médico.
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
                && diaDaSemana.getValue() <= DayOfWeek.SATURDAY.getValue()) {
            return  !hora.isBefore(LocalTime.of(8, 0)) && !hora.isAfter(LocalTime.of(19, 0));
        }

        return false;
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
        if (consultaRepository.existsByPacienteIdAndMedicoIdAndDataConsulta(pacienteId, medicoId, dataConsulta.toLocalDate())) {
            throw new PacienteComConsultaDuplicadaException(pacienteId,dataConsulta);
        }
    }

    private void garantirAntecedencia(LocalDateTime dataConsulta) {

        LocalDateTime agora = LocalDateTime.now();

        long minutosDeAntecedencia = Duration.between(agora,dataConsulta).toMinutes();

        if (minutosDeAntecedencia <= 30) {
            throw new AntecedenciaInsuficienteException();
        }
    }
}
