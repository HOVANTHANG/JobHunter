package com.example.Jobhunter.configuration;

import com.example.Jobhunter.Util.SecurityUtil;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    @Value("${thangka.jwt.base64-secret}")
    private String jwtKey;
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(cs->cs.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/login","/users").permitAll()
                        //.anyRequest().authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(formLogin ->formLogin.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        ;


        return http.build();
    }
//    @Bean
//    public JwtDecoder jwtDecoder(SecurityMetersService metersService) {
//        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
//        return token -> {
//            try {
//                return jwtDecoder.decode(token);
//            } catch (Exception e) {
//                if (e.getMessage().contains("Invalid signature")) {
//                    metersService.trackTokenInvalidSignature();
//                } else if (e.getMessage().contains("Jwt expired at")) {
//                    metersService.trackTokenExpired();
//                } else if (
//                        e.getMessage().contains("Invalid JWT serialization") ||
//                                e.getMessage().contains("Malformed token") ||
//                                e.getMessage().contains("Invalid unsecured/JWS/JWE")
//                ) {
//                    metersService.trackTokenMalformed();
//                } else {
//                    LOG.error("Unknown JWT error {}", e.getMessage());
//                }
//                throw e;
//            }
//        };
//    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtil.JWT_ALGORITHM.getName());
    }



}
