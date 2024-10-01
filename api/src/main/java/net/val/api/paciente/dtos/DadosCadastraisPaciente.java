package net.val.api.paciente.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import net.val.api.endereco.dtos.DadosEndereco;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record DadosCadastraisPaciente(
        @NotBlank
        String nome,
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate dataNasc,
        @NotNull
        double peso,
        @NotBlank
        String tipoSanguineo,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String telefone,
        @NotBlank
        @Pattern(regexp = "\\d{8}")
        String cpf,
        @Valid
        DadosEndereco endereco

) {
}
