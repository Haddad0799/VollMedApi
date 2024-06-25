package net.val.api.dtos.medicoDto;

import net.val.api.model.Endereco;
import net.val.api.model.Especialidade;
import net.val.api.model.Medico;

public record DadosMedicosAtualizados(
        Long id,
        String nome,
        String email,
        String telefone,
        Long crm,
        Especialidade especialidade,
        Endereco endereco,
        boolean ativo

) {

    public DadosMedicosAtualizados(Medico medico) {
        this(medico.getId(), medico.getNome(),
                medico.getEmail(), medico.getTelefone(),
                medico.getCrm(),medico.getEspecialidade(),medico.getEndereco(), medico.isAtivo());
    }
}
