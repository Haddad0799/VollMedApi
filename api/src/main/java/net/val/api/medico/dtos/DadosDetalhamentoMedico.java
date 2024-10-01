package net.val.api.medico.dtos;

import net.val.api.endereco.entity.Endereco;
import net.val.api.medico.enums.Especialidade;
import net.val.api.medico.entity.Medico;

public record DadosDetalhamentoMedico(
        Long id,
        String nome,
        String email,
        String telefone,
        Long crm,
        Especialidade especialidade,
        Endereco endereco,
        boolean ativo

) {

    public DadosDetalhamentoMedico(Medico medico) {
        this(medico.getId(), medico.getNome(),
                medico.getEmail(), medico.getTelefone(),
                medico.getCrm(),medico.getEspecialidade(),medico.getEndereco(), medico.isAtivo());
    }
}
