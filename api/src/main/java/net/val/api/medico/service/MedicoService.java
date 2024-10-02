package net.val.api.medico.service;

import lombok.SneakyThrows;
import net.val.api.endereco.entity.Endereco;
import net.val.api.endereco.repository.EnderecoRepository;
import net.val.api.medico.entity.Medico;
import net.val.api.medico.dtos.DadosAtualizacaoMedico;
import net.val.api.medico.dtos.DadosCadastraisMedico;
import net.val.api.medico.dtos.DadosDetalhamentoMedico;
import net.val.api.medico.dtos.DadosListagemMedico;
import net.val.api.infra.exceptions.medicoExceptions.MedicoNaoEncontradoException;
import net.val.api.medico.repository.MedicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EnderecoRepository enderecoRepository;

    public MedicoService(MedicoRepository medicoRepository, EnderecoRepository enderecoRepository) {
        this.medicoRepository = medicoRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public Medico cadastrarMedico(DadosCadastraisMedico dados) {

       return medicoRepository.save(new Medico(dados,enderecoRepository));
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
