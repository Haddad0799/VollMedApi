package net.val.api.consulta.service.cancelarConsulta;

import net.val.api.consulta.dtos.DadosCancelamentoConsulta;
import net.val.api.consulta.entity.Consulta;
import net.val.api.consulta.enums.StatusConsulta;
import net.val.api.consulta.repository.ConsultaRepository;
import net.val.api.consulta.service.cancelarConsulta.validacoes.ValidarCancelamentoConsulta;
import net.val.api.infra.exceptions.consultaExceptions.agendamentoExceptions.ConsultaNaoEncontrada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelarConsultaServiceTest {

    @Mock
    private  ConsultaRepository consultaRepository;

    @InjectMocks
    private CancelarConsultaService cancelarConsultaService;

    @Mock
    private ValidarCancelamentoConsulta validacao1;
    @Mock
    private ValidarCancelamentoConsulta validacao2;



    @BeforeEach
    public void setup() {
        List<ValidarCancelamentoConsulta> validarCancelamentoConsultas = new ArrayList<>();
        validarCancelamentoConsultas.add(validacao1);
        validarCancelamentoConsultas.add(validacao2);

        cancelarConsultaService = new CancelarConsultaService(consultaRepository,validarCancelamentoConsultas);
        
    }

    @Test
    @DisplayName("A consulta deve ter o status mudado para CANCELADA")
    void cancelarConsultaCenario1() {

        DadosCancelamentoConsulta dadosCancelamentoConsulta = new DadosCancelamentoConsulta(1L,
                "paciente desistiu");

        Consulta consultaMock = mock(Consulta.class);
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consultaMock));

        
        cancelarConsultaService.cancelarConsulta(dadosCancelamentoConsulta);

        verify(validacao1).validar(any(DadosCancelamentoConsulta.class));
        verify(validacao2).validar(any(DadosCancelamentoConsulta.class));
        verify(consultaMock).setStatus(StatusConsulta.CANCELADA);
        verify(consultaRepository).save(consultaMock);
    }

    @Test
    @DisplayName("Deve lançar uma exceção se a consulta não for encontrada")
    void cancelarConsultaCenario2() {

        DadosCancelamentoConsulta dadosCancelamentoConsulta = new DadosCancelamentoConsulta(1L,
                "paciente desistiu");

        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ConsultaNaoEncontrada.class,
                () -> cancelarConsultaService.cancelarConsulta(dadosCancelamentoConsulta));

    }


}