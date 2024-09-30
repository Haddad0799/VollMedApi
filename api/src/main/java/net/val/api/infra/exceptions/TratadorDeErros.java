package net.val.api.infra.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import net.val.api.domain.ApiErro;
import net.val.api.infra.exceptions.autenticacaoExceptions.UsuarioNaoEncontradoException;
import net.val.api.infra.exceptions.consultaExceptions.*;
import net.val.api.infra.exceptions.especialidadeExceptions.EspecialidadeInvalidaException;
import net.val.api.infra.exceptions.especialidadeExceptions.EspecialidadeNulaException;
import net.val.api.infra.exceptions.medicoExceptions.MedicoInativoException;
import net.val.api.infra.exceptions.medicoExceptions.MedicoNaoEncontradoException;
import net.val.api.infra.exceptions.pacienteExceptions.PacienteNaoEncontradoException;
import net.val.api.infra.exceptions.tipoSanguineoExceptions.TipoSanguineoInvalidoException;
import net.val.api.infra.exceptions.tokenExceptions.FalhaAoGerarTokenException;
import net.val.api.infra.exceptions.tokenExceptions.InvalidTokenException;
import net.val.api.infra.exceptions.tokenExceptions.TokenNotProvidedException;
import net.val.api.infra.exceptions.ufExceptions.UfInvalidaException;
import net.val.api.infra.exceptions.usuarioException.LoginJaUtilizadoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TratadorDeErros {

    private static final Logger logger = LoggerFactory.getLogger(TratadorDeErros.class);

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

    // Handler genérico para exceções BAD_REQUEST
    @ExceptionHandler({
            MedicoInativoException.class,
            ConflitoDeHorarioMedicoException.class,
            PacienteComConsultaDuplicadaException.class,
            ConflitoDeHorarioPacienteException.class,
            HorarioInvalidoConsultaException.class,
            TokenNotProvidedException.class,
            UfInvalidaException.class,
            TipoSanguineoInvalidoException.class,
            EspecialidadeInvalidaException.class,
            AntecedenciaInsuficienteException.class,
            HttpMessageNotReadableException.class,
            LoginJaUtilizadoException.class,
            EspecialidadeNulaException.class
    })
    public ResponseEntity<ApiErro> handleBadRequestExceptions(RuntimeException ex, HttpServletRequest request) {
        logger.error("Erro BAD_REQUEST: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // Handler para exceções de validação
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErro> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Detalha quais campos falharam na validação
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        logger.error("Erro de validação: {}", errorMessage);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, request);
    }

    // Handler genérico para exceções NOT_FOUND
    @ExceptionHandler({
            MedicoNaoEncontradoException.class,
            PacienteNaoEncontradoException.class,
            UsuarioNaoEncontradoException.class,
            InternalAuthenticationServiceException.class
    })
    public ResponseEntity<ApiErro> handleNotFoundExceptions(RuntimeException ex, HttpServletRequest request) {
        logger.error("Erro NOT_FOUND: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // Handler genérico para exceções UNAUTHORIZED
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiErro> handleUnauthorizedExceptions(InvalidTokenException ex, HttpServletRequest request) {
        logger.error("Erro UNAUTHORIZED: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    // Handler genérico para exceções INTERNAL_SERVER_ERROR
    @ExceptionHandler(FalhaAoGerarTokenException.class)
    public ResponseEntity<ApiErro> handleInternalServerErrorExceptions(FalhaAoGerarTokenException ex, HttpServletRequest request) {
        logger.error("Erro INTERNAL_SERVER_ERROR: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    // Handler genérico para exceções inesperadas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErro> handleGeneralExceptions(Exception ex, HttpServletRequest request) {
        logger.error("Erro inesperado: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado.", request);
    }
    
}
