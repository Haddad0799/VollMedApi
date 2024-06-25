package net.val.api.service;

import lombok.SneakyThrows;
import net.val.api.dtos.pacienteDto.DadosAtualizacaoPaciente;
import net.val.api.dtos.pacienteDto.DadosCadastraisPaciente;
import net.val.api.dtos.pacienteDto.DadosListagemPacientes;
import net.val.api.excepitions.EntityNotFoundException;
import net.val.api.model.Endereco;
import net.val.api.model.Paciente;
import net.val.api.repositorys.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class PacienteService {

        private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public void CadastrarPaciente(DadosCadastraisPaciente dadosCadastraisPaciente) {
        pacienteRepository.save(new Paciente(dadosCadastraisPaciente));
    }

    @Transactional
    public Page<DadosListagemPacientes> listarPacientes(Pageable paginacao) {
        return pacienteRepository.findAll(paginacao).map(DadosListagemPacientes::new);
    }

    @Transactional
    @SneakyThrows
    public Paciente atualizarPaciente(DadosAtualizacaoPaciente dadosAtualizacaoPaciente) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(dadosAtualizacaoPaciente.id());

        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();

            if (dadosAtualizacaoPaciente.nome() != null) {
                paciente.setNome(dadosAtualizacaoPaciente.nome());
            }

            if (dadosAtualizacaoPaciente.telefone() != null) {
                paciente.setTelefone(dadosAtualizacaoPaciente.telefone());
            }

            if (dadosAtualizacaoPaciente.peso() != null) {
                paciente.setPeso(dadosAtualizacaoPaciente.peso());
            }

            if (dadosAtualizacaoPaciente.endereco() != null) {
                Endereco.atualizarEndereco(paciente.getEndereco(), dadosAtualizacaoPaciente.endereco());
            }

            return pacienteRepository.save(paciente);
        } else {
            throw new EntityNotFoundException("Paciente com ID " + dadosAtualizacaoPaciente.id() + " não encontrado.");
        }
    }

    @SneakyThrows
    @Transactional
    public void excluirPaciente(Long id){
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(id);

        if(pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            if(paciente.isAtivo()) {
                paciente.setAtivo(false);
                pacienteRepository.save(paciente);
            }
        } else {
            throw new EntityNotFoundException("Paciente com ID " + id + " não encontrado.");
        }
    }

}
