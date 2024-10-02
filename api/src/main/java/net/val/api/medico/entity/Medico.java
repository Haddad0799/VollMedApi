package net.val.api.medico.entity;

import jakarta.persistence.*;
import lombok.*;
import net.val.api.endereco.entity.Endereco;
import net.val.api.endereco.repository.EnderecoRepository;
import net.val.api.medico.dtos.DadosCadastraisMedico;
import net.val.api.medico.enums.Especialidade;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String nome;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    @Setter
    private String telefone;
    @Column(unique = true)
    private Long crm;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @OneToOne
    @JoinColumn(name = "endereco_id")
    @Setter
    private Endereco endereco;

    @Setter
    private boolean ativo;



    public Medico(DadosCadastraisMedico dadosCadastrais, EnderecoRepository enderecoRepository) {
        this.nome = dadosCadastrais.nome();
        this.email = dadosCadastrais.email();
        this.telefone = dadosCadastrais.telefone();
        this.crm = Long.parseLong(dadosCadastrais.crm());
        this.especialidade = Especialidade.fromEspecialidade(dadosCadastrais.especialidade());
        this.endereco = enderecoRepository.save(new Endereco(dadosCadastrais.endereco()));
        this.ativo = true;
    }

    @Override
    public String toString() {
        return """
                Médico:%s
                Email:%s
                CRM:%d
                Especialidade:%s
                Endereco:%s
                """.formatted(nome,email,crm,especialidade,endereco);
    }
}

