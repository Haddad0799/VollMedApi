package net.val.api.service;

import net.val.api.dtos.pacienteDto.DadosCadastraisPaciente;
import net.val.api.dtos.pacienteDto.DadosListagemPacientes;
import net.val.api.model.Paciente;
import net.val.api.repositorys.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

}
