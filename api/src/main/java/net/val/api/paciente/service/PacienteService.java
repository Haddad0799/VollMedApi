package net.val.api.paciente.service;

import lombok.SneakyThrows;
import net.val.api.endereco.repository.EnderecoRepository;
import net.val.api.paciente.dtos.DadosAtualizacaoPaciente;
import net.val.api.paciente.dtos.DadosCadastraisPaciente;
import net.val.api.paciente.dtos.DadosDetalhamentoPaciente;
import net.val.api.paciente.dtos.DadosListagemPacientes;
import net.val.api.endereco.entity.Endereco;
import net.val.api.paciente.entity.Paciente;
import net.val.api.infra.exceptions.pacienteExceptions.PacienteNaoEncontradoException;
import net.val.api.paciente.repository.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class PacienteService {

        private final PacienteRepository pacienteRepository;
        private final EnderecoRepository enderecoRepository;

    public PacienteService(PacienteRepository pacienteRepository, EnderecoRepository enderecoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public Paciente CadastrarPaciente(DadosCadastraisPaciente dadosCadastraisPaciente) {
       return pacienteRepository.save(new Paciente(dadosCadastraisPaciente, enderecoRepository));
    }

    @Transactional
    public Page<DadosListagemPacientes> listarPacientes(Pageable paginacao) {
        return pacienteRepository.findAllByAtivoTrue(paginacao).map(DadosListagemPacientes::new);
    }

    @Transactional
    @SneakyThrows
    public Paciente atualizarPaciente(DadosAtualizacaoPaciente dadosAtualizacaoPaciente) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(dadosAtualizacaoPaciente.id());

        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();


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
            throw new PacienteNaoEncontradoException(dadosAtualizacaoPaciente.id());
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
            throw new PacienteNaoEncontradoException(id);
        }
    }

    @Transactional
    @SneakyThrows
    public DadosDetalhamentoPaciente detalharPaciente (Long pacienteId) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(pacienteId);

        if(pacienteOptional.isPresent()) {
            Paciente paciente = pacienteOptional.get();

            return new DadosDetalhamentoPaciente(paciente);
        }
        throw new PacienteNaoEncontradoException(pacienteId);
    }

}
