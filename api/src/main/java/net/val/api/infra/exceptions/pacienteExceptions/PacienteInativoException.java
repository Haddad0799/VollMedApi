package net.val.api.infra.exceptions.pacienteExceptions;

public class PacienteInativoException extends RuntimeException{
    public PacienteInativoException(Long idPaciente) {
        super("Paciente com o ID informado está inativo: " + idPaciente);
    }
}
