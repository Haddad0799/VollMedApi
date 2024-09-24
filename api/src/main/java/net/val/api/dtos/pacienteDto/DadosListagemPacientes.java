package net.val.api.dtos.pacienteDto;

import net.val.api.domain.Paciente;

import java.time.LocalDate;

public record DadosListagemPacientes(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNasc,
        int idade,
        double peso,
        String tipoSanguineo,
        LocalDate dataCadastro,
        boolean ativo


) {
    public DadosListagemPacientes (Paciente paciente) {
        this(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getDataNasc(),
                paciente.getIdade(),
                paciente.getPeso(),
                paciente.getTipoSanguineo().getDescricao(),
                paciente.getDataCadastro(),
                paciente.isAtivo()
                );
    }
}
