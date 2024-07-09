package net.val.api.infra;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class TratadorDeErros {

    private static final Logger log = LoggerFactory.getLogger(TratadorDeErros.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarErro404(EntityNotFoundException ex) {
        log.error("Entidade não encontrada", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErrosValidacao>> tratarErro400(MethodArgumentNotValidException ex) {
        log.error("Erro de validação", ex);
        var erros = ex.getFieldErrors();
        var dadosErros = erros.stream()
                .map(DadosErrosValidacao::new)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dadosErros);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> tratarErroIllegalArgument(IllegalArgumentException ex) {
        log.error("Argumento ilegal", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Argumento inválido: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> tratarErro500(Exception ex) {
        log.error("Erro interno do servidor", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
    }

    @Getter
    public static class DadosErrosValidacao {
        private final String campo;
        private final String mensagem;

        public DadosErrosValidacao(String campo, String mensagem) {
            this.campo = campo;
            this.mensagem = mensagem;
        }

        public DadosErrosValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}