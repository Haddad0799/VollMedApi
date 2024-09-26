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


    private ResponseEntity<ApiErro> buildErrorResponse(HttpStatus status, String message, HttpServletRequest request) {
        ApiErro apiErro = new ApiErro(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiErro, status);
    }

    // Handlers para exceções específicas
    @ExceptionHandler(MedicoNaoEncontradoException.class)
    public ResponseEntity<ApiErro> handleMedicoNaoEncontradoException(MedicoNaoEncontradoException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(MedicoInativoException.class)
    public ResponseEntity<ApiErro> handleMedicoInativoException(MedicoInativoException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(PacienteNaoEncontradoException.class)
    public ResponseEntity<ApiErro> handlePacienteNaoEncontradoException(PacienteNaoEncontradoException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(HorarioInvalidoConsultaException.class)
    public ResponseEntity<ApiErro> handleHorarioInvalidoConsultaException(HorarioInvalidoConsultaException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(TokenNotProvidedException.class)
    public ResponseEntity<ApiErro> handleTokenNotProvidedException(TokenNotProvidedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiErro> handleInvalidTokenException(InvalidTokenException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(FalhaAoGerarTokenException.class)
    public ResponseEntity<ApiErro> handleFalhaAoGerarTokenException(FalhaAoGerarTokenException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ApiErro> handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

}
