package net.val.api.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class TratadorDeErros {

    private static final Logger log = LoggerFactory.getLogger(TratadorDeErros.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<List<DadosErrosValidacao>> tratarErro404(EntityNotFoundException ex) {
        log.error("Entidade não encontrada", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of(new DadosErrosValidacao("id", "Entidade não encontrada")));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErrosValidacao>> tratarErro400(MethodArgumentNotValidException ex) {
        log.error("Erro de validação", ex);
        var erros = ex.getFieldErrors();
        var dadosErros = erros.stream()
                .map(DadosErrosValidacao::new)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dadosErros);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<List<DadosErrosValidacao>> tratarErroIllegalArgument(IllegalArgumentException ex) {
        log.error("Argumento ilegal", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of(new DadosErrosValidacao("argumento", ex.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<List<DadosErrosValidacao>> tratarErro500(Exception ex) {
        log.error("Erro interno do servidor", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of(new DadosErrosValidacao("servidor", "Ocorreu um erro interno no servidor.")));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<List<DadosErrosValidacao>> tratarErro403(BadCredentialsException ex) {
        log.error("Credenciais inválidas!", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(List.of(new DadosErrosValidacao("credenciais", "Credenciais inválidas! Verifique se a senha e login estão corretos.")));
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
