package net.val.api.service;

import net.val.api.domain.Consulta;
import net.val.api.domain.Medico;
import net.val.api.domain.Paciente;
import net.val.api.dtos.consultaDto.DadosAgendamentoConsulta;
import net.val.api.infra.exceptions.consultaExceptions.HorarioInvalidoConsultaException;
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

    public ConsultaService(@Lazy ConsultaRepository consultaRepository,@Lazy MedicoRepository medicoRepository,@Lazy PacienteRepository pacienteRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public Consulta agendarConsulta(DadosAgendamentoConsulta agendamentoConsulta) {
       Medico medico = medicoRepository.findById(
               agendamentoConsulta.medicoId()).orElseThrow(
                       () -> new MedicoNaoEncontradoException(agendamentoConsulta.medicoId()));

       Paciente paciente = pacienteRepository.findById(
               agendamentoConsulta.pacienteId()).orElseThrow(
                       () -> new PacienteNaoEncontradoException(agendamentoConsulta.pacienteId()));

       if(!medico.isAtivo()) {
           throw new MedicoInativoException(medico.getId());
       }
       if (!dataIsValid(agendamentoConsulta.dataConsulta())) {
           throw new HorarioInvalidoConsultaException();
       }
        Consulta consulta = new Consulta(agendamentoConsulta, medico,paciente);
        consultaRepository.save(consulta);

       return consulta;
    }

    public boolean dataIsValid(LocalDateTime data) {
        DayOfWeek diaDaSemana = data.getDayOfWeek();
        LocalTime hora = data.toLocalTime();

        if (diaDaSemana == DayOfWeek.SUNDAY) {
            return false;
        }

        if (diaDaSemana.getValue() >= DayOfWeek.MONDAY.getValue()
                && diaDaSemana.getValue() <= DayOfWeek.FRIDAY.getValue()){
            return hora.isBefore(LocalTime.of(17,0)) && !hora.isBefore(LocalTime.of(8,0));
        }

        if(diaDaSemana.getValue() == DayOfWeek.SATURDAY.getValue()) {
            return hora.isBefore(LocalTime.of(11,0)) && !hora.isBefore(LocalTime.of(8,0));
        }
        return false;
    }
}