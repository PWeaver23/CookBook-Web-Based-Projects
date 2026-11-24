package com.cookbook.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;


@Service 
public class TokenService {

    private final JwtEncoder encoder;
    private final long minutes;

    public TokenService(JwtEncoder encoder, @Value("${cookbook.token-minutes}") long minutes) {
        this.encoder = encoder;
        this.minutes = minutes;
    }

    public long minutesValid() {
        return minutes;
    }

    /** checked: the result of checking the username and password at login. */
    public String issue(Authentication checked) {
        List<String> roles = checked.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .filter(name -> name.startsWith("ROLE_"))
                .map(name -> name.substring("ROLE_".length()))     // "ROLE_USER" -> "USER"
                .toList();
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("cookbook")
                .subject(checked.getName())                 // who: the username
                .issuedAt(now)
                .expiresAt(now.plus(minutes, ChronoUnit.MINUTES))
                .claim("roles", roles)
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
    
}
