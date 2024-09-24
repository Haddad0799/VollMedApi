package net.val.api.dtos.medicoDto;

import net.val.api.domain.Endereco;
import net.val.api.domain.Especialidade;
import net.val.api.domain.Medico;

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
