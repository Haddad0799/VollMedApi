package net.val.api.infra.exceptions.consultaExceptions;

public class AntecedenciaInsuficienteException extends RuntimeException{
    public AntecedenciaInsuficienteException() {
        super("A consulta deve ser realizada com pelo menos 30 minutos de antecedência.");
    }
}
