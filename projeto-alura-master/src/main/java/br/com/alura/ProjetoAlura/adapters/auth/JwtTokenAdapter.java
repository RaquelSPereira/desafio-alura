package br.com.alura.ProjetoAlura.adapters.auth;

import br.com.alura.ProjetoAlura.models.token.PayloadToken;
import br.com.alura.ProjetoAlura.services.auth.AuthService;
import br.com.alura.ProjetoAlura.services.userDetails.UserDetailsImpl;
import br.com.alura.ProjetoAlura.util.exceptions.UnauthorizedException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class JwtTokenAdapter implements AuthService {

    @Value ("${spring.jwt.key}")
    private String SECRET_KEY;

    @Value ("${spring.jwt.issuer}")
    private String ISSUER;

    public String generateToken(UserDetailsImpl user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(creationDate())
                    .withExpiresAt(expirationDate())
                    .withSubject(user.getUsername())
                    .withClaim("role", user.getRole().name())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new JWTCreationException("Erro ao gerar token.", exception);
        }
    }

    public PayloadToken getPayloadFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);

            String email = decodedJWT.getSubject();
            String role = decodedJWT.getClaim("role").asString();

            return new PayloadToken(email, role);
        } catch (JWTVerificationException exception){
            throw new UnauthorizedException("Token inválido ou expirado.");
        }
    }

    private Instant creationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant();
    }

    private Instant expirationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusHours(4).toInstant();
    }
}
