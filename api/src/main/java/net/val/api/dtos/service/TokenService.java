package net.val.api.dtos.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.SneakyThrows;
import net.val.api.infra.exceptions.tokenExceptions.FalhaAoGerarTokenException;
import net.val.api.infra.exceptions.tokenExceptions.InvalidTokenException;
import net.val.api.domain.Usuario;
import net.val.api.infra.exceptions.tokenExceptions.TokenNotProvidedException;
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
            throw new FalhaAoGerarTokenException();
        }
    }

    @SneakyThrows
    public String getSubject(String token)  {


        try {
            var algoritmo = Algorithm.HMAC256(jwtSecret);

            return JWT.require(algoritmo)
                    .withIssuer("API Voll.Med")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTDecodeException  | TokenExpiredException ex) {
            throw new InvalidTokenException();
        }
    }
}
