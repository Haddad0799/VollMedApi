package net.val.api.service;

import net.val.api.dtos.medicoDto.DadosAtualizacaoMedico;
import net.val.api.dtos.medicoDto.DadosCadastraisMedico;
import net.val.api.dtos.medicoDto.DadosListagemMedico;
import net.val.api.excepitions.MedicoNotFoundException;
import net.val.api.model.Endereco;
import net.val.api.model.Medico;
import net.val.api.repositorys.MedicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public void cadastrarMedico(DadosCadastraisMedico dados) {
        medicoRepository.save(new Medico(dados));
    }

    @Transactional
    public Page<DadosListagemMedico> listarMedicos(Pageable paginacao) {
        return medicoRepository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemMedico::new);
    }

    @Transactional
    public void atualizar(DadosAtualizacaoMedico dados) {
        Optional<Medico> medicoOptional = medicoRepository.findById(dados.id());

        medicoOptional.ifPresentOrElse(m -> {
            if (dados.nome() != null) {
                m.setNome(dados.nome());
            }
            if (dados.telefone() != null) {
                m.setTelefone(dados.telefone());
            }
            if (dados.endereco() != null) {
                Endereco.atualizarEndereco(m.getEndereco(), dados.endereco());
            }
        }, () -> {
            throw new MedicoNotFoundException("Médico não encontrado com o ID: " + dados.id());
        });
    }

    @Transactional
    public void excluir(Long id) {
        Optional<Medico> medicoOptional = medicoRepository.findById(id);
        if (medicoOptional.isPresent()) {
            Medico medico = medicoOptional.get();
            medico.setAtivo(false);
            medicoRepository.save(medico);
        } else {
            throw new MedicoNotFoundException("Médico não encontrado com o ID: " + id);
        }
    }

}
