package net.val.api.endereco.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.val.api.endereco.enums.UF;
import net.val.api.endereco.dtos.DadosEndereco;

@Getter
@Setter
@Entity
@Table(name = "enderecos")
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String logradouro;
    private int numero;
    private String complemento;
    private String bairro;
    private String cidade;

    @Enumerated(EnumType.STRING)
    private UF uf;

    private String cep;

    public Endereco(DadosEndereco dadosEndereco) {
        this.logradouro = dadosEndereco.logradouro();
        this.numero = dadosEndereco.numero();
        this.complemento = dadosEndereco.complemento();
        this.bairro = dadosEndereco.bairro();
        this.cidade = dadosEndereco.cidade();
        this.uf = UF.fromUf(dadosEndereco.uf());
        this.cep = dadosEndereco.cep();
    }

    public static void atualizarEndereco(Endereco endereco, DadosEndereco dadosEndereco) {
        if (dadosEndereco.logradouro() != null) {
            endereco.setLogradouro(dadosEndereco.logradouro());
        }
        if (dadosEndereco.numero() != 0) {
            endereco.setNumero(dadosEndereco.numero());
        }
        if (dadosEndereco.complemento() != null) {
            endereco.setComplemento(dadosEndereco.complemento());
        }
        if (dadosEndereco.bairro() != null) {
            endereco.setBairro(dadosEndereco.bairro());
        }
        if (dadosEndereco.cidade() != null) {
            endereco.setCidade(dadosEndereco.cidade());
        }
        if (dadosEndereco.uf() != null) {
            endereco.setUf(UF.fromUf(dadosEndereco.uf()));
        }
        if (dadosEndereco.cep() != null) {
            endereco.setCep(dadosEndereco.cep());
        }
    }

    @Override
    public String toString() {
        return """
               {
                Logradouro:%s
                Número:%d
                Complemento:%s
                Bairro:%s
                Cidade:%s
                UF:%s
                CEP:%S
                }
               """.formatted(logradouro, numero, complemento, bairro, cidade, uf, cep);
    }
}
