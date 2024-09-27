package net.val.api.infra.exceptions.ufExceptions;

public class UfInvalidaException extends RuntimeException{
    public UfInvalidaException(String uf) {
        super("Nenhuma UF com o valor: " + uf);
    }
}
