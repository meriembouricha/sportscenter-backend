package com.ecomerce.sportscenter.Security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.entity.Roles;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String generateToken(AppUser appUser) {
        System.out.println("Génération du token pour: " + appUser.getUsername());
        System.out.println("ID de l'utilisateur: " + appUser.getId());
        return Jwts.builder()
                .setSubject(appUser.getUsername())
                .claim("id", appUser.getId())
                .claim("role", appUser.getRoles().stream().map(Roles::name).collect(Collectors.joining(","))) // Ajouter les rôles
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
