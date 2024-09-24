package net.val.api.dtos.medicoDto;

import net.val.api.domain.Especialidade;
import net.val.api.domain.Medico;

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
