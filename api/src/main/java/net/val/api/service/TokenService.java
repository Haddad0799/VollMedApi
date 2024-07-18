package net.val.api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import net.val.api.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(jwtSecret);
            Instant tempoDeExpiracao = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            return JWT.create()
                    .withIssuer("API Voll.Med")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(tempoDeExpiracao)
                    .sign(algoritmo);
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Falha ao gerar token JWT", ex);
        }
    }
}
