package net.val.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    public Consulta(DadosAgendamentoConsulta dadosAgendamentoConsulta, Medico medico, Paciente paciente) {
        this.paciente = paciente;
        this.medico = medico;
        this.dataConsulta = dadosAgendamentoConsulta.dataConsulta();
    }
}
