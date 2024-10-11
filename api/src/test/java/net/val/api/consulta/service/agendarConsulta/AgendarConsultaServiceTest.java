package net.val.api.consulta.service.agendarConsulta;

import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.consulta.entity.Consulta;
import net.val.api.consulta.repository.ConsultaRepository;
import net.val.api.consulta.service.agendarConsulta.validacoesDeAgendamento.ValidarAgendamentoConsulta;
import net.val.api.infra.exceptions.medicoExceptions.MedicoNaoEncontradoException;
import net.val.api.infra.exceptions.pacienteExceptions.PacienteNaoEncontradoException;
import net.val.api.medico.entity.Medico;
import net.val.api.medico.enums.Especialidade;
import net.val.api.medico.repository.MedicoRepository;
import net.val.api.paciente.entity.Paciente;
import net.val.api.paciente.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendarConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;
    @Mock
    private MedicoRepository medicoRepository;
    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ValidarAgendamentoConsulta validacao1;
    @Mock
    private ValidarAgendamentoConsulta validacao2;

    @InjectMocks
    private AgendarConsultaService agendarConsultaService;

    private DadosAgendamentoConsulta dadosAgendamentoConsulta;

    @BeforeEach
    public void setup() {
        // Inicializando os dados de agendamento
        dadosAgendamentoConsulta = new DadosAgendamentoConsulta(1L, null, LocalDateTime.now(), "cardiologia");

        // Inicializando a lista de validações com os mocks
        List<ValidarAgendamentoConsulta> validacoesAgendamentoConsulta = new ArrayList<>();
        validacoesAgendamentoConsulta.add(validacao1);
        validacoesAgendamentoConsulta.add(validacao2);

        // Injetando a lista no serviço
        agendarConsultaService = new AgendarConsultaService(
                consultaRepository,
                medicoRepository,
                pacienteRepository,
                validacoesAgendamentoConsulta
        );
    }

    @Test
    @DisplayName("Deve realizar o agendamento da consulta se tudo ocorrer como esperado")
    void agendarConsultaCenario1() {

        Paciente pacienteMock = mock(Paciente.class);
        Medico medicoMock = mock(Medico.class);

        // Mockando os repositórios e entidades
        when(pacienteRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(pacienteMock));
        when(medicoRepository.medicoAletorio(any(Especialidade.class))).thenReturn(Optional.of(medicoMock));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(mock(Consulta.class));

        // Chama o método a ser testado
        agendarConsultaService.agendarConsulta(dadosAgendamentoConsulta);

        // Verifica se as validações foram chamadas corretamente
        verify(validacao1).validar(any(DadosAgendamentoConsulta.class));
        verify(validacao2).validar(any(DadosAgendamentoConsulta.class));
    }


    @Test
    @DisplayName("Deve lançar erro de paciente não encontrado")
    void agendarConsultaCenario2() {

        when(pacienteRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.empty());

        assertThrows(PacienteNaoEncontradoException.class, () -> agendarConsultaService.agendarConsulta(dadosAgendamentoConsulta));

    }

    @Test
    @DisplayName("Deve lançar erro de medico não encontrado")
    void agendarConsultaCenario3() {

        Paciente pacienteMock = mock(Paciente.class);

        when(pacienteRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(pacienteMock));
        when(medicoRepository.medicoAletorio(any(Especialidade.class))).thenReturn(Optional.empty());

        assertThrows(MedicoNaoEncontradoException.class, () -> agendarConsultaService.agendarConsulta(dadosAgendamentoConsulta));

    }
}