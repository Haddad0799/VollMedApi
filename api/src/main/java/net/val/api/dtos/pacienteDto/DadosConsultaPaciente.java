package net.val.api.dtos.pacienteDto;

import net.val.api.domain.Paciente;

import java.time.LocalDate;

public record DadosConsultaPaciente(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNasc,
        int idade,
        double peso,
        String tipoSanguineo
) {
    public DadosConsultaPaciente (Paciente paciente){
        this(paciente.getId(), paciente.getNome(), paciente.getCpf(),
                paciente.getDataNasc(), paciente.getIdade(), paciente.getPeso(), String.valueOf(paciente.getTipoSanguineo()));
    }
}
