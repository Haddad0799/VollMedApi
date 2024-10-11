package net.val.api.paciente.repository;

import net.val.api.paciente.dtos.DadosCadastraisPaciente;
import net.val.api.paciente.entity.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@Nested
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PacienteRepositoryTest {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("deve retornar o paciente referente ao ID informado se ele estiver ativo")
    void findByIdAndAndAtivoTrueCenario1() {
        Paciente paciente = cadastrarPaciente(
                "João da Silva",
                LocalDate.of(1985, 5, 15),
                75.5,
                "O+",
                "joao.silva@exemplo.com",
                "61987654321",
                "12345678"
        );

        var pacienteAtivo = pacienteRepository.findByIdAndAtivoTrue(paciente.getId());

        assertThat(pacienteAtivo).isPresent();
    }

    @Test
    @DisplayName("Não deve retornar o paciente referente ao ID informado se ele estiver inativo")
    void findByIdAndAndAtivoTrueCenario2() {
        Paciente paciente = cadastrarPaciente(
                "João da Silva",
                LocalDate.of(1985, 5, 15),
                75.5,
                "O+",
                "joao.silva@exemplo.com",
                "61987654321",
                "12345678"
        );

        paciente.setAtivo(false);

        var pacienteInativo = pacienteRepository.findByIdAndAtivoTrue(paciente.getId());


        assertThat(pacienteInativo).isEmpty();
    }

    private Paciente cadastrarPaciente(
            String nome,
            LocalDate dataNasc,
            double peso,
            String tipoSanguineo,
            String email,
            String telefone,
            String cpf
    ) {
        // Cria os dados cadastrais do paciente com base nos parâmetros fornecidos
        DadosCadastraisPaciente dadosCadastraisPaciente = new DadosCadastraisPaciente(
                nome,
                dataNasc,
                peso,
                tipoSanguineo,
                email,
                telefone,
                cpf,
                null
        );

        // Cria a instância do paciente com os dados cadastrados
        Paciente paciente = new Paciente(dadosCadastraisPaciente);

        // Persiste o paciente no banco de dados
        em.persist(paciente);

        // Retorna o paciente cadastrado para possíveis verificações no teste
        return paciente;
    }
}