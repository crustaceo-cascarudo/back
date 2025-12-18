package com.fpmislata.back.infrastructure;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fpmislata.back.domain.repository.UserRepository;
import com.fpmislata.back.domain.repository.entity.UserEntity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class TokenFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/users/register",
            "/api/users/login");

    private static final List<String> PUBLIC_GET_PATHS = Arrays.asList(
            "/api/categories",
            "/api/products",
            "/api/ingredients");

    private static final List<String> AUTHENTICATED_PATHS = Arrays.asList(
            "/api/users/logout");

    public TokenFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private boolean isPublicGetPath(String path) {
        return PUBLIC_GET_PATHS.stream().anyMatch(path::startsWith);
    }

    private boolean isAuthenticatedPath(String path) {
        return AUTHENTICATED_PATHS.stream().anyMatch(path::startsWith);
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = String.format(
                "{\"error\":\"%s\",\"status\":401,\"timestamp\":\"%s\"}",
                message,
                java.time.Instant.now().toString());
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        String requestMethod = request.getMethod();

        System.out.println("[TokenFilter] " + requestMethod + " " + requestPath);

        if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
            System.out.println("OPTIONS request (CORS preflight), permitir sin validación");
            filterChain.doFilter(request, response);
            return;
        }

        // Verificar si es ruta pública
        if (isPublicPath(requestPath)) {
            System.out.println("Ruta pública, permitir sin token");
            filterChain.doFilter(request, response);
            return;
        }

        // Verificar si es GET público
        if (isPublicGetPath(requestPath) && "GET".equalsIgnoreCase(requestMethod)) {
            System.out.println("GET público, permitir sin token");
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer token de la cabecera Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Token no encontrado o formato inválido");
            sendUnauthorizedResponse(response, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("Token extraído: " + token.substring(0, Math.min(token.length(), 20)) + "...");

        // Validar token en la base de datos
        UserEntity user;
        try {
            user = userRepository.findByToken(token);
        } catch (Exception e) {
            System.out.println("Error consultando token en BD: " + e.getMessage());
            sendUnauthorizedResponse(response, "Authentication service error");
            return;
        }

        if (user == null) {
            System.out.println("Token inválido o expirado");
            sendUnauthorizedResponse(response, "Invalid or expired token");
            return;
        }

        System.out.println("Usuario autenticado: " + user.name() + " (Role: " + user.role() + ")");

        // Agregar información del usuario al request
        request.setAttribute("authenticatedUser", user);
        request.setAttribute("authenticatedUserId", user.id());
        request.setAttribute("authenticatedUserRole", user.role());
        request.setAttribute("authenticatedUserName", user.name());

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
