package net.val.api.medico.dtos;

import net.val.api.medico.enums.Especialidade;
import net.val.api.medico.entity.Medico;

public record DadosConsultaMedico(
        Long id,
        String nome,
        Long crm,
        Especialidade especialidade

) {

    public DadosConsultaMedico(Medico medico) {
        this(medico.getId(), medico.getNome(), medico.getCrm(), medico.getEspecialidade());
    }
}
