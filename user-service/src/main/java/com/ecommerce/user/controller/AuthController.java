package com.ecommerce.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    
    /**
     * Kullanıcı girişi endpoint'i
     * 1. Kullanıcı bilgilerini doğrula
     * 2. JWT token oluştur
     * 3. Token'ı response olarak dön
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Kullanıcı bilgilerini doğrula
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            // Hatalı giriş durumunda 401 dön
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(false, "Invalid username or password"));
        }
        
        // Kullanıcı bilgilerini yükle
        final UserDetails userDetails = userDetailsService
            .loadUserByUsername(loginRequest.getUsername());
            
        // JWT token oluştur
        final String token = jwtTokenUtil.generateToken(userDetails);
        
        // Token'ı response olarak dön
        return ResponseEntity.ok(new JwtResponse(token));
    }
} 