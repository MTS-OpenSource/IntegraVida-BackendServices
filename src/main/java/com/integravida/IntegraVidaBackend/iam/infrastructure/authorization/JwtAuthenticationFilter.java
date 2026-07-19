package com.integravida.IntegraVidaBackend.iam.infrastructure.authorization;

import com.integravida.IntegraVidaBackend.iam.infrastructure.tokens.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public JwtAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = tokenService.extractUsername(token);
            } catch (Exception e) {
                logger.error("Error al extraer el username del token JWT", e);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (tokenService.validateToken(token)) {
                String role = tokenService.extractRole(token);

                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", tokenService.extractUserId(token));
                claims.put("role", role);
                claims.put("profileId", tokenService.extractProfileId(token));
                claims.put("patientId", tokenService.extractPatientId(token));
                claims.put("doctorId", tokenService.extractDoctorId(token));

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, authorities
                );

                authToken.setDetails(claims);

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
