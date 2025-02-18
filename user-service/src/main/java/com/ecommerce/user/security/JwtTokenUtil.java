package com.ecommerce.user.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret; // Token'ı imzalamak için kullanılan gizli anahtar
    
    @Value("${jwt.expiration}")
    private Long expiration; // Token'ın geçerlilik süresi (saniye)
    
    /**
     * Kullanıcı bilgilerinden JWT token oluşturur
     * Header: Algoritma bilgisi (HS512)
     * Payload: Kullanıcı bilgileri (username, roller, oluşturma/bitiş tarihi)
     * Signature: Header ve Payload'ın secret key ile imzalanması
     */
    public String generateToken(UserDetails userDetails) {
        // Claims: Token içinde taşınacak bilgiler
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()));
            
        return Jwts.builder()
            .setClaims(claims) // Kullanıcı rollerini ekle
            .setSubject(userDetails.getUsername()) // Kullanıcı adını ekle
            .setIssuedAt(new Date(System.currentTimeMillis())) // Token oluşturma zamanı
            .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)) // Token bitiş zamanı
            .signWith(SignatureAlgorithm.HS512, secret) // Token'ı imzala
            .compact(); // Token'ı oluştur
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
    }
    
    private Boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
} 