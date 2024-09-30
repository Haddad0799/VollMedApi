package net.val.api.dtos.consultaDto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


public record DadosAgendamentoConsulta(
        @NotNull
        @JsonAlias("paciente_Id")
        Long pacienteId,

        @JsonAlias("medico_Id")
        Long medicoId,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "America/Sao_Paulo")
        @FutureOrPresent
        @JsonAlias("data_Consulta")
        LocalDateTime dataConsulta,


        @JsonAlias("medico_Especialidade")
        String especialidadeMedica
        ) {


}
