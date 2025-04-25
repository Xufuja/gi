package dev.xfj.app.services;

import dev.xfj.app.security.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader("Authorization");

        if (apiKey == null || !apiKey.equals("REAL_TEST_PREFIX:REAL_TEST_KEY")) {
            throw new BadCredentialsException("{\"error\": \"Invalid Authorization!\"}");
        }

        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}