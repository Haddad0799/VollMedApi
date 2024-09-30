package net.val.api.repositorys;

import net.val.api.domain.Especialidade;
import net.val.api.domain.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Page<Medico> findAllByAtivoTrue(Pageable paginacao);

    @Query("select m from Medico m where m.ativo = true and m.especialidade = :especialidade order by function('RAND')")
    Optional<Medico> medicoAletorio(Especialidade especialidade);

}
