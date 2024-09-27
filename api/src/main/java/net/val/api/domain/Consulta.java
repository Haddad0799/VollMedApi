package net.val.api.domain;

import jakarta.persistence.*;
import lombok.*;
import net.val.api.dtos.consultaDto.DadosAgendamentoConsulta;

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
    private Especialidade especialidadeMedica;

    @Enumerated(EnumType.STRING)
    @Setter
    private StatusConsulta status;

    public Consulta(DadosAgendamentoConsulta dadosAgendamentoConsulta, Medico medico, Paciente paciente) {
        this.paciente = paciente;
        this.medico = medico;
        this.dataConsulta = dadosAgendamentoConsulta.dataConsulta();
        this.especialidadeMedica = Especialidade.fromEspecialidade(dadosAgendamentoConsulta.especialidadeMedica());
        this.status = StatusConsulta.AGENDADA;
    }
}
