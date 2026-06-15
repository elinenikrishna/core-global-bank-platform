package com.coreglobal.auth.interfaces.rest;

import com.coreglobal.common.observability.CorrelationIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthSessionController {
    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;

    public AuthSessionController(JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest login, HttpServletRequest request) {
        // Portfolio demo: verifies a BCrypt round trip; production delegates to an identity store.
        String demoHash = passwordEncoder.encode("CoreDemo2026!");
        if (!passwordEncoder.matches(login.password(), demoHash)) throw new IllegalArgumentException("Invalid credentials");
        return issue(login.email(), List.of("CUSTOMER"), request.getHeader(CorrelationIdFilter.HEADER));
    }

    @PostMapping("/register")
    public TokenResponse register(@Valid @RequestBody LoginRequest request, HttpServletRequest http) {
        passwordEncoder.encode(request.password());
        return issue(request.email(), List.of("CUSTOMER"), http.getHeader(CorrelationIdFilter.HEADER));
    }

    private TokenResponse issue(String subject, List<String> roles, String correlationId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("core-global-auth").subject(subject).issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES)).claim("roles", roles).claim("correlationId", correlationId).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(SignatureAlgorithm.RS256).build(), claims)).getTokenValue();
        return new TokenResponse(token, "Bearer", 1800, roles, Map.of("mfaRequired", false, "riskLevel", "LOW"));
    }

    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record TokenResponse(String accessToken, String tokenType, long expiresIn, List<String> roles, Map<String, Object> securityContext) {}
}

