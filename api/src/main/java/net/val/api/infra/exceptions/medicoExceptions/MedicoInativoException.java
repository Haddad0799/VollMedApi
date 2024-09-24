package net.val.api.infra.exceptions.medicoExceptions;

public class MedicoInativoException extends  RuntimeException{
    public MedicoInativoException(Long id){
        super("Médico com ID " + id + " está inativo.");
    }
}
