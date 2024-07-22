package net.val.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import net.val.api.infra.exceptions.TokenNotProvidedException;
import net.val.api.repositorys.UsuarioRepository;
import net.val.api.service.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
        try {
            String tokenJwt = recuperarToken(request);

            if (tokenJwt != null) {
                var subject = tokenService.getSubject(tokenJwt);
                Optional<UserDetails> userDetails = usuarioRepository.findByLogin(subject);

                if (userDetails.isPresent()) {
                    var usuario = userDetails.get();
                    var usuarioAutenticado = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usuarioAutenticado);
                }
            }

            // Chamando os próximos filtros caso houver.
            filterChain.doFilter(request, response);

        } catch (TokenNotProvidedException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"campo\": \"token\", \"mensagem\": \"" + ex.getMessage() + "\"}");
        }
    }
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        throw new TokenNotProvidedException("Token não fornecido!");
    }
}