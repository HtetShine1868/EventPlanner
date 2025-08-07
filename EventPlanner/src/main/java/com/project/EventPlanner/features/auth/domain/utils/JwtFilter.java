package com.project.EventPlanner.features.auth.domain.utils;

import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;

import com.project.EventPlanner.security.CustomUserDetail;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ 1. Skip token checks for public auth endpoints
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 1. Check if Authorization header is present and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                // invalid token or expired
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserDetails userDetails = new CustomUserDetail(user);

            if (jwtUtil.isTokenValid(token, userDetails)) {
                String role = jwtUtil.extractRole(token); // e.g., "USER"
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                // ✅ Inject authority manually
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // 🔍 ADD THESE:
                System.out.println("✅ Setting authenticated user: " + user.getUsername());
                System.out.println("✅ Role from token: " + role);

                SecurityContextHolder.getContext().setAuthentication(authToken);

                // 🔍 Confirm:
                System.out.println("✅ SecurityContext set: " + SecurityContextHolder.getContext().getAuthentication());
            } else {
                System.out.println("❌ Token is not valid.");

            }
        }
        System.out.println("➡️ Continuing with chain. Status: " + response.getStatus());

        // 4. Continue the filter chain
        filterChain.doFilter(request, response);
        System.out.println("👉 JwtFilter triggered");
        System.out.println("Authorization header: " + request.getHeader("Authorization"));
        System.out.println("Extracted token: " + token);
        System.out.println("Extracted username: " + username);

    }
}

