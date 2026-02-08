package com.hscp.user.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GatewayAuthFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(GatewayAuthFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        log.info("➡️ [User-Service] Incoming request: {} {}", method, path);

        String userId = request.getHeader("X-User-Id");
        String rolesHeader = request.getHeader("X-Roles");

        if (userId == null || rolesHeader == null) {
            log.info("🟡 No gateway auth headers found (public or unauthenticated request)");
            chain.doFilter(request, response);
            return;
        }

        log.info("🔐 Gateway headers received");
        log.info("👤 X-User-Id: {}", userId);
        log.info("🎭 X-Roles: {}", rolesHeader);

        List<SimpleGrantedAuthority> authorities =
                Arrays.stream(rolesHeader.split(","))
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .toList();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        authorities
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        log.debug("✅ SecurityContext populated for userId={} with roles={}",
                userId,
                authorities
        );

        chain.doFilter(request, response);
    }
}
