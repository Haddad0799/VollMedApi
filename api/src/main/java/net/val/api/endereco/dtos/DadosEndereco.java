package net.val.api.endereco.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import net.val.api.endereco.entity.Endereco;

public record DadosEndereco(

        @NotBlank
        String logradouro,
        int numero,
        String complemento,
        @NotBlank
        String bairro,
        @NotBlank
        String cidade,
        @NotBlank
        String uf,
        @NotBlank
        @Pattern(regexp = "\\d{8}")
        String cep
) {
        public DadosEndereco(Endereco endereco) {
                this(
                        endereco.getLogradouro(),
                        endereco.getNumero(),
                        endereco.getComplemento(),
                        endereco.getBairro(),
                        endereco.getCidade(),
                        endereco.getUf().name(),
                        endereco.getCep()
                );
        }
}
