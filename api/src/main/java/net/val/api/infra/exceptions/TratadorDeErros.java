package net.val.api.infra.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import net.val.api.domain.ApiErro;
import net.val.api.infra.exceptions.autenticacaoExceptions.UsuarioNaoEncontradoException;
import net.val.api.infra.exceptions.consultaExceptions.HorarioInvalidoConsultaException;
import net.val.api.infra.exceptions.medicoExceptions.MedicoInativoException;
import net.val.api.infra.exceptions.medicoExceptions.MedicoNaoEncontradoException;
import net.val.api.infra.exceptions.pacienteExceptions.PacienteNaoEncontradoException;
import net.val.api.infra.exceptions.tokenExceptions.FalhaAoGerarTokenException;
import net.val.api.infra.exceptions.tokenExceptions.InvalidTokenException;
import net.val.api.infra.exceptions.tokenExceptions.TokenNotProvidedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class TratadorDeErros {


    @ExceptionHandler(MedicoNaoEncontradoException.class)
    public ResponseEntity<ApiErro> handleMedicoNaoEncontradoException(MedicoNaoEncontradoException ex, HttpServletRequest request) {
        ApiErro apiErro = new ApiErro(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
                );
        return new ResponseEntity<>(apiErro, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (MedicoInativoException.class)
    public ResponseEntity<ApiErro> handleMedicoInativoException(MedicoInativoException ex, HttpServletRequest request) {
        ApiErro apiErro = new ApiErro(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(apiErro,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PacienteNaoEncontradoException.class)
    public ResponseEntity<ApiErro> handlePacienteNaoEncontradoException(PacienteNaoEncontradoException ex, HttpServletRequest request) {
        ApiErro apiErro = new ApiErro(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(apiErro,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HorarioInvalidoConsultaException.class)
    public ResponseEntity<ApiErro> handleHorarioInvalidoConsultaException(HorarioInvalidoConsultaException ex, HttpServletRequest request) {
        ApiErro apiErro = new ApiErro(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(apiErro,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenNotProvidedException.class)
    public ResponseEntity<ApiErro> handleTokenNotProvidedException(TokenNotProvidedException ex, HttpServletRequest request) {
        ApiErro apiErro = new ApiErro(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(apiErro,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiErro> handleInvalidTokenException(InvalidTokenException ex, HttpServletRequest request) {
        ApiErro apiErro = new ApiErro(LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(apiErro,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FalhaAoGerarTokenException.class)
    public ResponseEntity<ApiErro> handleFalhaAoGerarTokenException(FalhaAoGerarTokenException ex, HttpServletRequest request) {
        ApiErro apiErro = new ApiErro(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(apiErro,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ApiErro> handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex, HttpServletRequest request) {
        ApiErro apiErro = new ApiErro(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(apiErro,HttpStatus.NOT_FOUND);
    }

}
