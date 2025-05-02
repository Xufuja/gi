package dev.xfj.app.services;

import dev.xfj.app.entities.Keys;
import dev.xfj.app.repositories.KeysRepository;
import dev.xfj.app.security.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class AuthenticationService {
    private final KeysRepository keysRepository;
    @Autowired
    private PasswordEncoder encoder;

    public AuthenticationService(KeysRepository keysRepository) {
        this.keysRepository = keysRepository;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (!authorization.contains(":")) {
            unauthorized();
        }

        String[] apiKey = authorization.split(":");

        if (apiKey.length != 2) {
            unauthorized();
        }

        String decoded = "";

        try {
            decoded = fromBase64(apiKey[1]);
        } catch (Exception e) {
            unauthorized();
        }

        if (!encoder.matches(decoded, getSecret(apiKey[0]))) {
            unauthorized();
        }

        return new ApiKeyAuthentication(apiKey[0], AuthorityUtils.NO_AUTHORITIES);
    }

    private void unauthorized() {
        throw new BadCredentialsException("{\"error\": \"Invalid Authorization!\"}");
    }

    private String getSecret(String id) {
        Optional<Keys> key = keysRepository.findById(id);

        if (key.isEmpty()) {
            unauthorized();
        }

        return fromBase64(key.get().getSecret());
    }

    private String toBase64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    private String fromBase64(String value) {
        return new String(Base64.getDecoder().decode(value));
    }
}