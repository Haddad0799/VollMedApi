package net.val.api.infra.exceptions.consultaExceptions;

public class ConsultaNaoEncontrada extends RuntimeException{
    public ConsultaNaoEncontrada(Long id) {
        super("nenhuma consulta encontrada com ID: " + id);
    }
}
