package net.val.api.model;

import jakarta.persistence.*;
import lombok.*;
import net.val.api.dtos.pacienteDto.DadosCadastraisPaciente;

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
    private String nome;
    private String cpf;
    @Setter
    private String telefone;
    @Embedded
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
        this.tipoSanguineo = TipoSanguineo.fromDescricao(dadosCadastraisPaciente.tipoSanguineo());
        this.dataNasc = dadosCadastraisPaciente.dataNasc();
        this.peso = dadosCadastraisPaciente.peso();
        this.dataCadastro = LocalDate.now();
        this.idade = calcularIdade(this.dataNasc, this.dataCadastro);
        this.ativo = true;
    }

    private int calcularIdade(LocalDate dataNasc, LocalDate dataAtual) {
        if ((dataNasc != null) && (dataAtual != null)) {
            return Period.between(dataNasc, dataAtual).getYears();
        } else {
            return 0;
        }
    }


}

