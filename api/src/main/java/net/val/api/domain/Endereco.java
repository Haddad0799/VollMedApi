package net.val.api.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;
import net.val.api.dtos.enderecoDto.DadosEndereco;

@Getter
@Setter
@Embeddable
public class Endereco {
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
        this.uf = UF.fromString(dadosEndereco.uf());
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
            endereco.setUf(UF.fromString(dadosEndereco.uf()));
        }
        if (dadosEndereco.cep() != null) {
            endereco.setCep(dadosEndereco.cep());
        }
    }


    public Endereco() {}

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
