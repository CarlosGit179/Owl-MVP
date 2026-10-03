package br.com.carlos.Owl.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.carlos.Owl.entity.User;

/** Creates and validates signed JSON Web Tokens used for stateless authentication. */
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Creates a signed token whose subject is the user's login and whose issuer is Owl Api.
     *
     * @param user authenticated user to encode in the token
     * @return the signed token
     * @throws RuntimeException if token creation fails
     */
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create().withIssuer("Owl Api").withSubject(user.getLogin())
                    .withExpiresAt(genExpirationDate()).sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    /**
     * Verifies the token signature, issuer, and expiration.
     *
     * @param token token to validate
     * @return the login encoded as the token subject, or an empty string if invalid
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm).withIssuer("Owl Api").build().verify(token).getSubject();

        } catch (JWTVerificationException exception) {
            return "";

        }
    }

    /**
     * Calculates the token expiration time two hours from the current time.
     *
     * @return token expiration instant
     */
    public Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
