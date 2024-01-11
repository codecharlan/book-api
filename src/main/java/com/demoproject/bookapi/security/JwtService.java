package com.demoproject.bookapi.security;

import com.demoproject.bookapi.enums.Role;
import com.demoproject.bookapi.security.implementation.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {
    @Value("${jwt.secretToken}")
    private String secretToken;
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretToken);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication, Role role) {
        String email = authentication.getName();
        String fullName = ((UserDetailsImpl) authentication.getPrincipal()).getFullName();

        Date currentDate = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + 60000 * 120);

        return Jwts.builder()
                .setSubject(email)
                .claim("name", fullName)
                .claim("email", email)
                .claim("role", role.name())
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid Token");
        }
    }

    public  Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Map<String, String> validateTokenAndReturnDetail(String secretToken) {
        if (Boolean.FALSE.equals(validateToken(secretToken))) {
            throw new RuntimeException("Invalid Token");
        }
        var claim = getClaimsFromToken(secretToken);
        return Map.of("name", claim.get("name", String.class),
                "email", claim.get("email", String.class),
                "role", claim.get("role", String.class));
    }
}
