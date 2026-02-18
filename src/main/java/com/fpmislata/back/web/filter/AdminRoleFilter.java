package com.fpmislata.back.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fpmislata.back.domain.enumerado.Role;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(2)
public class AdminRoleFilter extends OncePerRequestFilter {

  // Rutas que requieren rol ADMIN
  private static final List<String> ADMIN_REQUIRED_PATHS = Arrays.asList(
      "/api/categories",
      "/api/products",
      "/api/ingredients",
      "/api/users");

  private static final List<String> ADMIN_EXCEPTIONS = Arrays.asList(
      "/api/users/register",
      "/api/users/login",
      "/api/users/logout");
  // Métodos que requieren ADMIN (escritura)
  private static final List<String> ADMIN_REQUIRED_METHODS = Arrays.asList(
      "POST", "PUT", "DELETE");

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    String requestPath = request.getRequestURI();
    String requestMethod = request.getMethod();

    System.out.println("[AdminRoleFilter] " + requestMethod + " " + requestPath);

    // Permitir OPTIONS sin validación (CORS preflight)
    if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
      System.out.println("OPTIONS request, permitir sin validación");
      filterChain.doFilter(request, response);
      return;
    }

    if (ADMIN_EXCEPTIONS.stream().anyMatch(requestPath::startsWith)) {
      System.out.println("Ruta de excepción, no requiere ADMIN");
      filterChain.doFilter(request, response);
      return;
    }

    boolean requiresAdmin = requiresAdminRole(requestPath, requestMethod) ||
        (requestPath.startsWith("/api/users") && "GET".equalsIgnoreCase(requestMethod));

    if (requiresAdmin) {
      Role userRole = (Role) request.getAttribute("authenticatedUserRole");

      if (userRole == null) {
        System.out.println("No hay usuario autenticado");
        sendForbiddenResponse(response, "Authentication required");
        return;
      }

      if (userRole != Role.ADMIN) {
        System.out.println("Acceso denegado: se requiere rol ADMIN (usuario: " + userRole + ")");
        sendForbiddenResponse(response, "Admin privileges required for this operation");
        return;
      }

      System.out.println("Rol ADMIN verificado");
    } else {
      System.out.println("No requiere verificación de rol ADMIN");
    }

    // Continuar con la cadena de filtros
    filterChain.doFilter(request, response);
  }

  /**
   * Determina si la ruta y método requieren rol ADMIN
   */
  private boolean requiresAdminRole(String path, String method) {
    boolean isAdminPath = ADMIN_REQUIRED_PATHS.stream().anyMatch(path::startsWith);
    boolean isAdminMethod = ADMIN_REQUIRED_METHODS.contains(method);
    return isAdminPath && isAdminMethod;
  }

  /**
   * Envía respuesta 403 Forbidden
   */
  private void sendForbiddenResponse(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String jsonResponse = String.format(
        "{\"error\":\"%s\",\"status\":403,\"timestamp\":\"%s\"}",
        message,
        java.time.Instant.now().toString());
    response.getWriter().write(jsonResponse);
  }
}
