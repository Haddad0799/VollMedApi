package net.val.api.paciente.entity;

import jakarta.persistence.*;
import lombok.*;
import net.val.api.endereco.entity.Endereco;
import net.val.api.paciente.enums.TipoSanguineo;
import net.val.api.paciente.dtos.DadosCadastraisPaciente;

import java.time.LocalDate;
import java.time.Period;

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
    @Setter
    @Column(unique = true)
    private String nome;
    @Column(unique = true)
    private String cpf;
    @Setter
    @Column(unique = true)
    private String telefone;
    @OneToOne
    @JoinColumn(name = "endereco_id")
    @Setter
    private Endereco endereco;
    @Enumerated(EnumType.STRING)
    private TipoSanguineo tipoSanguineo;
    private LocalDate dataNasc;
    private LocalDate dataCadastro;
    private int idade;
    @Setter
    private double peso;
    @Setter
    private boolean ativo;


    public Paciente(DadosCadastraisPaciente dadosCadastraisPaciente) {
        this.nome = dadosCadastraisPaciente.nome();
        this.cpf = dadosCadastraisPaciente.cpf();
        this.telefone = dadosCadastraisPaciente.telefone();
        this.endereco = new Endereco(dadosCadastraisPaciente.endereco());
        this.tipoSanguineo = TipoSanguineo.fromTipo(dadosCadastraisPaciente.tipoSanguineo());
        this.dataNasc = dadosCadastraisPaciente.dataNasc();
        this.peso = dadosCadastraisPaciente.peso();
        this.dataCadastro = LocalDate.now();
        this.idade = calcularIdade(this.dataNasc);
        this.ativo = true;
    }

    private int calcularIdade(LocalDate dataNasc) {
            if (dataNasc != null) {
                LocalDate dataAtual = LocalDate.now();
                return Period.between(dataNasc, dataAtual).getYears();
            }
            else {
            return 0;
        }
    }


}

