package net.val.api.consulta.repository;

import net.val.api.consulta.dtos.DadosAgendamentoConsulta;
import net.val.api.consulta.entity.Consulta;
import net.val.api.medico.dtos.DadosCadastraisMedico;
import net.val.api.medico.entity.Medico;
import net.val.api.paciente.dtos.DadosCadastraisPaciente;
import net.val.api.paciente.entity.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest //Utilizado para testar Repositorys
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // configura o teste para utilizar o database da aplicação e não um em memória.
@ActiveProfiles("test") // Configuração para utilização do ambiente de teste.
class ConsultaRepositoryTest {

   @Autowired
   private ConsultaRepository consultaRepository;

   @Autowired
   private TestEntityManager em;

    @Test
    @DisplayName("deve retornar true se o medico escolhido ja possuir uma consulta no horario digitado")
    void existsByMedicoIdAndDataConsultaBetweenCenario1() {

        Paciente paciente = cadastrarPaciente(
                "João da Silva",
                LocalDate.of(1985, 5, 15),
                75.5,
                "O+",
                "joao.silva@exemplo.com",
                "61987654321",
                "12345678"
        );

        Paciente paciente2 = cadastrarPaciente(
                "felipe freitas",
                LocalDate.of(1990, 10, 20),
                80.5,
                "A+",
                "felipe.freitas@exemplo.com",
                "6298736542",
                "89102345"
        );



        Medico medico =   cadastrarMedico("luis",
                "medicoVoll@gmail.com",
                "61987653421",
                "123456",
                "cardiologia");


        LocalDateTime dataConsulta = LocalDateTime.of(2024, 10, 5, 14, 0);

        DadosAgendamentoConsulta dadosAgendamentoConsulta = new DadosAgendamentoConsulta(paciente.getId(), medico.getId(), dataConsulta,null);
        DadosAgendamentoConsulta dadosAgendamentoConsulta2 = new DadosAgendamentoConsulta(paciente2.getId(), medico.getId(), dataConsulta,null);
        cadastrarConsulta(dadosAgendamentoConsulta,medico,paciente);
        cadastrarConsulta(dadosAgendamentoConsulta2, medico,paciente2);

        boolean consultaExistente = consultaRepository.existsByMedicoIdAndDataConsultaBetween(medico.getId(),dataConsulta.minusMinutes(59),dataConsulta.plusMinutes(59));

        assertThat(consultaExistente).isTrue();

    }

    @Test
    @DisplayName("deve retornar false se não houver consultas agendadas dentro do intervalo.")
    void existsByMedicoIdAndDataConsultaBetweenCenario2() {
        // Cadastra os pacientes
        Paciente paciente = cadastrarPaciente("João da Silva", LocalDate.of(1985, 5, 15), 75.5, "O+", "joao.silva@exemplo.com", "61987654321", "12345678");
        Paciente paciente2 = cadastrarPaciente("Felipe Freitas", LocalDate.of(1990, 10, 20), 80.5, "A+", "felipe.freitas@exemplo.com", "6298736542", "89102345");

        // Cadastra o médico
        Medico medico = cadastrarMedico("Luis", "medicoVoll@gmail.com", "61987653421", "123456", "cardiologia");

        // Definição das datas das consultas
        LocalDateTime dataConsulta1 = LocalDateTime.of(2024, 10, 5, 14, 0); // Consulta agendada para 14:00
        LocalDateTime dataConsulta2 = LocalDateTime.of(2024, 10, 5, 16, 0);// Consulta agendada para 16:00
        LocalDateTime dataConsulta3 = LocalDateTime.of(2024, 10, 5, 15, 0); // Consulta agendada para 15:00
        // Cria dados para as consultas
        DadosAgendamentoConsulta dadosAgendamentoConsulta1 = new DadosAgendamentoConsulta(paciente.getId(), medico.getId(), dataConsulta1, null);
        DadosAgendamentoConsulta dadosAgendamentoConsulta2 = new DadosAgendamentoConsulta(paciente2.getId(), medico.getId(), dataConsulta2, null);

        // Cadastra as consultas
        cadastrarConsulta(dadosAgendamentoConsulta1, medico, paciente);
        cadastrarConsulta(dadosAgendamentoConsulta2, medico, paciente2);

        // Verifica se existe consulta no intervalo de 59 minutos antes e depois de uma nova consulta
        boolean consultaExistente = consultaRepository.existsByMedicoIdAndDataConsultaBetween(
                medico.getId(),
                dataConsulta3.minusMinutes(59), //14:01
                dataConsulta3.plusMinutes(59)   //15:59
        );

        // Verifica o resultado esperado
        assertThat(consultaExistente).isFalse(); // Deve retornar false pois não existe consulta agendada nesse intervalo
    }


    private void cadastrarConsulta(DadosAgendamentoConsulta dadosAgendamentoConsulta, Medico medico, Paciente paciente) {
        Consulta consulta = new Consulta(dadosAgendamentoConsulta,medico,paciente);
        em.persist(consulta);

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

    private Medico cadastrarMedico(String nome, String email, String telefone, String crm, String especialidade) {
        DadosCadastraisMedico dadosCadastraisMedico = new DadosCadastraisMedico(
                nome, email, telefone, crm, especialidade, null
        );
        Medico medico = new Medico(dadosCadastraisMedico);
        em.persist(medico); // Persiste o médico no banco de dados para o teste
        return medico; // Retorna o médico cadastrado para as verificações
    }
}