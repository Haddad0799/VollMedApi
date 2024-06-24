package net.val.api.dtos.medicoDto;

import net.val.api.model.Medico;

public record DadosListagemMedico(
        Long id,
        String nome,
        String email,
        String crm,
        String especialidade
) {

    public DadosListagemMedico (Medico medico){
        this(medico.getId(), medico.getNome()
                ,medico.getEmail(),
                 medico.getCrm().toString(),
                medico.getEspecialidade().toString());
    }
}

