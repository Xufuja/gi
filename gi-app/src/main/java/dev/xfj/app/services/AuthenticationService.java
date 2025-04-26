package dev.xfj.app.services;

import dev.xfj.app.security.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private static final String TEST_KEY = "REAL_TEST_KEY";
    private static final String TEST_SECRET = "REAL_TEST_SECRET";

    public Authentication getAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (!authorization.contains(":")) {
            unauthorized();
        }

        String[] apiKey = authorization.split(":");

        if (apiKey.length != 2) {
            unauthorized();
        }

        if (getSecret(apiKey[0]).equals("")) {
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
        if (key.equals(TEST_KEY)) {
            return TEST_SECRET;
        }

        return "";
    }
}