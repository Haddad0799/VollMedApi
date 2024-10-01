package net.val.api.paciente.dtos;

import net.val.api.paciente.entity.Paciente;

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
