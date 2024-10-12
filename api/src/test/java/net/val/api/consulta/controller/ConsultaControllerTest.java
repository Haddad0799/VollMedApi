package net.val.api.consulta.controller;

import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.consulta.dtos.DadosDetalhamentoConsulta;
import net.val.api.consulta.entity.Consulta;
import net.val.api.consulta.service.agendarConsulta.AgendarConsultaService;
import net.val.api.consulta.service.cancelarConsulta.CancelarConsultaService;
import net.val.api.medico.entity.Medico;
import net.val.api.paciente.entity.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@SpringBootTest
class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendarConsultaService agendarConsultaService;

    @MockBean
    private CancelarConsultaService cancelarConsultaService;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> agendamentoConsultaJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> detalhamentoConsultaJson;

    @Test
    @DisplayName("Deveria devolver o erro 400 quando a requisição estiver inválida")
    @WithMockUser
    void agendarCenario1() throws Exception {
        var response = mockMvc.perform(post("/consultas/agendar"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria agendar a consulta com sucesso (201 created)")
    @WithMockUser
    void agendarCenario2() throws Exception {

        Consulta consultaMock = mock(Consulta.class);
        Paciente pacienteMock = mock(Paciente.class);
        Medico medicoMock  = mock(Medico.class);

        LocalDateTime dataConsulta = LocalDateTime.of(2024, 10, 20, 14, 0);

        var dadosAgendamento = new DadosAgendamentoConsulta(
                1L,
                1L,
                dataConsulta,
                "ortopedia"
        );

        when(agendarConsultaService.agendarConsulta(dadosAgendamento)).thenReturn(consultaMock);
        when(consultaMock.getMedico()).thenReturn(medicoMock);
        when(consultaMock.getPaciente()).thenReturn(pacienteMock);

        DadosDetalhamentoConsulta dadosDetalhamentoConsulta = new DadosDetalhamentoConsulta(
                consultaMock,
                consultaMock.getMedico(),
                consultaMock.getPaciente()
        );

        String Token = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJBUEkgVm9sbC5NZWQiLCJzdWIiOiJoYWRkYWQ2NjIwIiwiZXhwIjoxNzI4Nzg4NDAwfQ.8sNYJfROumhbmgfkvc9DmxRYhbBpqn8txQEdm4zgwx8";

        var response = mockMvc.perform(post("/consultas/agendar")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", Token) // Adiciona o cabeçalho Authorization
                .content(agendamentoConsultaJson.write(dadosAgendamento).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonEsperado = detalhamentoConsultaJson.write(dadosDetalhamentoConsulta).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
}

