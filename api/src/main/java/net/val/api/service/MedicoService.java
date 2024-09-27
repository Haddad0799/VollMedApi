package net.val.api.service;

import lombok.SneakyThrows;
import net.val.api.domain.Endereco;
import net.val.api.domain.Medico;
import net.val.api.dtos.medicoDto.DadosAtualizacaoMedico;
import net.val.api.dtos.medicoDto.DadosCadastraisMedico;
import net.val.api.dtos.medicoDto.DadosDetalhamentoMedico;
import net.val.api.dtos.medicoDto.DadosListagemMedico;
import net.val.api.infra.exceptions.medicoExceptions.MedicoNaoEncontradoException;
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

    public Medico cadastrarMedico(DadosCadastraisMedico dados) {

       return medicoRepository.save(new Medico(dados));
    }

    @Transactional
    public Page<DadosListagemMedico> listarMedicos(Pageable paginacao) {
        return medicoRepository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemMedico::new);
    }

    @Transactional
    public Medico atualizar(DadosAtualizacaoMedico dados) {
        Optional<Medico> medicoOptional = medicoRepository.findById(dados.id());

        if (medicoOptional.isPresent()) {
            Medico medico = medicoOptional.get();
            if (dados.nome() != null) {
                medico.setNome(dados.nome());
            }
            if (dados.telefone() != null) {
                medico.setTelefone(dados.telefone());
            }
            if (dados.endereco() != null) {
                Endereco.atualizarEndereco(medico.getEndereco(), dados.endereco());
            }
            return medicoRepository.save(medico);
        } else {
            throw new MedicoNaoEncontradoException(dados.id());
        }
    }

    @Transactional
    public void excluir(Long id) {
        Optional<Medico> medicoOptional = medicoRepository.findById(id);
        if (medicoOptional.isPresent()) {
            Medico medico = medicoOptional.get();
            medico.setAtivo(false);
            medicoRepository.save(medico);
        } else {
            throw new MedicoNaoEncontradoException(id);
        }
    }

    @Transactional
    @SneakyThrows
    public DadosDetalhamentoMedico detalharMedico(Long medicoId) {
        Optional<Medico> medicoOptional = medicoRepository.findById(medicoId);

        if(medicoOptional.isPresent()) {
            Medico medico = medicoOptional.get();

            return new DadosDetalhamentoMedico(medico);
        }
        throw new MedicoNaoEncontradoException(medicoId);
    }

}
