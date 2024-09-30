package net.val.api.repositorys;

import net.val.api.domain.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("""
    select case when count(c) > 0 then true else false end
    from Consulta c\s
    where c.medico.id = :medicoId
    and c.status = 'AGENDADA'
    and c.dataConsulta between :inicio and :fim
""")
    boolean existsByMedicoIdAndDataConsultaBetween(@Param("medicoId") Long medicoId,
                                                   @Param("inicio") LocalDateTime inicio,
                                                   @Param("fim") LocalDateTime fim);

    @Query("""
    select case when count(c) > 0 then true else false end
    from Consulta c\s
    where c.paciente.id = :pacienteId
    and c.status = 'AGENDADA'
    and c.dataConsulta between :inicio and :fim
    
""")
    boolean existsByPacienteIdAndDataConsultaBetween(@Param("pacienteId") Long pacienteId,
                                                     @Param("inicio") LocalDateTime inicio,
                                                     @Param("fim") LocalDateTime fim);

    @Query("""
    SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END\s
    FROM Consulta c
    WHERE c.paciente.id = :pacienteId\s
    AND c.medico.id = :medicoId\s
    AND c.status = 'AGENDADA'
    AND c.dataConsulta BETWEEN :inicio AND :fim
""")
    boolean existsByPacienteIdAndMedicoIdAndDataConsulta(@Param("pacienteId") Long pacienteId,
                                                         @Param("medicoId") Long medicoId,
                                                         @Param("inicio") LocalDateTime inicio,
                                                         @Param("fim") LocalDateTime fim);

    @Query("SELECT c FROM Consulta c WHERE c.status = 'AGENDADA'")
    Page<Consulta> findAllAgendadas(Pageable pageable);

}
