package dev.xfj.app.services;

import dev.xfj.app.security.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
public class AuthenticationService {
    private static final Map<String, String> TEST_KEYS = Map.of(
            "REAL_TEST_KEY", "REAL_TEST_SECRET",
            "SECOND_TEST_KEY", "SECOND_TEST_SECRET"
    );

    public Authentication getAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (!authorization.contains(":")) {
            unauthorized();
        }

        String[] apiKey = authorization.split(":");

        if (apiKey.length != 2) {
            unauthorized();
        }

        if (!getSecret(apiKey[0]).equals(apiKey[1])) {
            unauthorized();
        }

        return new ApiKeyAuthentication(authorization, AuthorityUtils.NO_AUTHORITIES);
    }

    private void unauthorized() {
        throw new BadCredentialsException("{\"error\": \"Invalid Authorization!\"}");
    }

    private String getSecret(String key) {
        if (!TEST_KEYS.containsKey(key)) {
            unauthorized();
        }

        return Base64.getEncoder().encodeToString(TEST_KEYS.get(key).getBytes());
    }
}