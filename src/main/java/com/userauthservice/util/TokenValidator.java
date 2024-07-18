package com.userauthservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import static io.jsonwebtoken.Jwts.parser;

@Component
public class TokenValidator implements Serializable {

    @Value("${jwt.token.validity}")
    public long TOKEN_VALIDITY;

    @Value("${jwt.signing.key}")
    public String SIGNING_KEY;

    @Value("${jwt.authorities.key}")
    public String AUTHORITIES_KEY;

    @Value("${userservice.app.jwtSecret}")
    private String jwtSecret;



    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserNameFromJwtToken(String token) {
        return parser().setSigningKey(getSignKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public void validateToken(final String token) {
        Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}