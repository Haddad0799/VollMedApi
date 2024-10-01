package net.val.api.paciente.dtos;

import net.val.api.endereco.entity.Endereco;
import net.val.api.paciente.entity.Paciente;

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
