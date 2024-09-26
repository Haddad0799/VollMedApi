package net.val.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import net.val.api.infra.exceptions.tokenExceptions.InvalidTokenException;
import net.val.api.infra.exceptions.tokenExceptions.TokenNotProvidedException;
import net.val.api.repositorys.UsuarioRepository;
import net.val.api.service.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class FiltroDeSeguranca extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UsuarioRepository usuarioRepository;


    public FiltroDeSeguranca(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        if (request.getRequestURI().equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String tokenJwt = recuperarToken(request);
            var subject = tokenService.getSubject(tokenJwt);
            Optional<UserDetails> userDetails = usuarioRepository.findByLogin(subject);

            if (userDetails.isPresent()) {
                var usuario = userDetails.get();
                var usuarioAutenticado = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usuarioAutenticado);
            }
        } catch (TokenNotProvidedException ex) {
            // Resposta para token não fornecido
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(String.format("{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"Bad Request\",\"message\":\"%s\",\"path\":\"%s\"}",
                    LocalDateTime.now(), HttpServletResponse.SC_BAD_REQUEST, "Token não enviado no cabeçalho!", request.getRequestURI()));
            return; // Retorna para não continuar o filtro
        } catch (InvalidTokenException ex) {
            // Resposta para token inválido
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(String.format("{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"Unauthorized\",\"message\":\"%s\",\"path\":\"%s\"}",
                    LocalDateTime.now(), HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage(), request.getRequestURI()));
            return;
        }
        // Chamando os próximos filtros caso não haja erro.
        filterChain.doFilter(request, response);
    }
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        throw new TokenNotProvidedException();
    }
}