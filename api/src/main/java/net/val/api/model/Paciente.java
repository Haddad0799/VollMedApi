package net.val.api.model;

import jakarta.persistence.*;
import lombok.*;
import net.val.api.dtos.pacienteDto.DadosCadastraisPaciente;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pacientes")
@EqualsAndHashCode(of = "id")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    @Embedded
    private Endereco endereco;
    @Enumerated(EnumType.STRING)
    private TipoSanguineo tipoSanguineo;
    private LocalDate dataNasc;
    private LocalDate dataCadastro;
    private int idade;
    private double peso;

    public Paciente(DadosCadastraisPaciente dadosCadastraisPaciente) {
        this.nome = dadosCadastraisPaciente.nome();
        this.cpf = dadosCadastraisPaciente.cpf();
        this.telefone = dadosCadastraisPaciente.telefone();
        this.endereco = new Endereco(dadosCadastraisPaciente.endereco());
        this.tipoSanguineo = TipoSanguineo.fromDescricao(dadosCadastraisPaciente.tipoSanguineo());
        this.dataNasc = dadosCadastraisPaciente.dataNasc();
        this.idade = dadosCadastraisPaciente.idade();
        this.peso = dadosCadastraisPaciente.peso();
        this.dataCadastro = LocalDate.now();
    }

}

