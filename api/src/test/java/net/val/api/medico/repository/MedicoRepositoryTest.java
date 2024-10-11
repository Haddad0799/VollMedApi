package net.val.api.medico.repository;

import net.val.api.medico.dtos.DadosCadastraisMedico;
import net.val.api.medico.entity.Medico;
import net.val.api.medico.enums.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@Nested
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Deveria devolver null se não houver médicos disponíveis para a especialidade da consulta")
    void medicoAleatorioCenario1() {
        Especialidade especialidade = Especialidade.ORTOPEDIA;

        // Cadastra um médico com uma especialidade diferente
        cadastrarMedico("luis", "medicoVoll@gmail.com", "61987653421", "123456", "cardiologia");

        // Tenta buscar um médico para a especialidade que não está cadastrada
        var medicoAleatorio = medicoRepository.medicoAletorio(especialidade);

        // Verifica que o retorno é null, pois não há médicos com essa especialidade
        assertThat(medicoAleatorio).isEmpty();
    }

    @Test
    @DisplayName("Deve selecionar um médico aleatório para a especialidade escolhida")
    void medicoAleatorioCenario2() {
        Especialidade especialidade = Especialidade.ORTOPEDIA;

        // Cadastra um médico com uma especialidade igual
        Medico medicoOrtopedista = cadastrarMedico("joao", "medVoll@gmail.com", "6291763542", "109876", "ortopedia");

        // Busca um médico aleatório com a especialidade igual
        var medicoAleatorio = medicoRepository.medicoAletorio(especialidade);

        // Verifica se o médico existe e se a especialidade é igual
        assertThat(medicoAleatorio).isPresent();
        assertThat(medicoAleatorio.get()).isEqualTo(medicoOrtopedista);
    }

    private Medico cadastrarMedico(String nome, String email, String telefone, String crm, String especialidade) {
        DadosCadastraisMedico dadosCadastraisMedico = new DadosCadastraisMedico(
                nome, email, telefone, crm, especialidade, null
        );
        Medico medico = new Medico(dadosCadastraisMedico);
        em.persist(medico); // Persiste o médico no banco de dados para o teste
        return medico; // Retorna o médico cadastrado para as verificações
    }

    @Test
    @DisplayName("Deve retornar um medico que esteja ativo e corresponda ao id fornecido")
    void findByIdAndAndAtivoTrueCenario1() {
        Medico medico = cadastrarMedico("joao", "medVoll@gmail.com", "6291763542", "109876", "ortopedia");

        var medicoAtivo = medicoRepository.findByIdAndAndAtivoTrue(medico.getId());

        assertThat(medicoAtivo).isPresent();

    }

    @Test
    @DisplayName("Não deve retornar o medico correspondente ao id se ele estiver inativo")
    void findByIdAndAndAtivoTrueCenario2() {
        Medico medico = cadastrarMedico("joao", "medVoll@gmail.com", "6291763542", "109876", "ortopedia");
        medico.setAtivo(false);

        var medicoInativo = medicoRepository.findByIdAndAndAtivoTrue(medico.getId());

        assertThat(medicoInativo).isEmpty();
    }
}
