package net.val.api.consulta.entity;

import jakarta.persistence.*;
import lombok.*;
import net.val.api.medico.enums.Especialidade;
import net.val.api.medico.entity.Medico;
import net.val.api.paciente.entity.Paciente;
import net.val.api.consulta.enums.StatusConsulta;
import net.val.api.consulta.dtos.DadosAgendamentoConsulta;

import java.time.LocalDateTime;

@Entity
@Table (name = "consultas")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Paciente paciente;

    @ManyToOne
    private Medico medico;

    private LocalDateTime dataConsulta;

    @Enumerated(EnumType.STRING)
    @Setter
    private Especialidade especialidadeMedica;

    @Enumerated(EnumType.STRING)
    @Setter
    private StatusConsulta status;

    public Consulta(DadosAgendamentoConsulta dadosAgendamentoConsulta, Medico medico, Paciente paciente) {
        this.paciente = paciente;
        this.medico = medico;
        this.dataConsulta = dadosAgendamentoConsulta.dataConsulta();
        if (dadosAgendamentoConsulta.especialidadeMedica() == null) {
            this.setEspecialidadeMedica(medico.getEspecialidade());
        } else {
            this.especialidadeMedica = Especialidade.fromEspecialidade(dadosAgendamentoConsulta.especialidadeMedica());
        }
        this.status = StatusConsulta.AGENDADA;
    }
}
