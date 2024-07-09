package net.val.api.dtos.pacienteDto;

import net.val.api.model.Endereco;
import net.val.api.model.Paciente;

import java.time.LocalDate;

public record DadosDetalhamentoPaciente(
        Long id,
        String nome,
        LocalDate dataNasc,
        int idade,
        double peso,
        String tipoSanguineo,
        String telefone,
        String cpf,
        Endereco endereco
) {
    public DadosDetalhamentoPaciente(Paciente paciente){
        this(paciente.getId(), paciente.getNome(), paciente.getDataNasc()
                ,paciente.getIdade(),
                paciente.getPeso(),
                paciente.getTipoSanguineo().getDescricao(),paciente.getTelefone(),
                paciente.getCpf(), paciente.getEndereco());
    }
}
