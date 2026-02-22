package com.fpmislata.back.web.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fpmislata.back.domain.enumerado.Role;
import com.fpmislata.back.domain.repository.UserRepository;
import com.fpmislata.back.domain.repository.entity.UserEntity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class TokenFilterTest {

    private UserRepository userRepository;
    private TokenFilter tokenFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        tokenFilter = new TokenFilter(userRepository);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void publicPath_register_allowsWithoutToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/users/register");
        when(request.getMethod()).thenReturn("POST");

        tokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void publicPath_login_allowsWithoutToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/users/login");
        when(request.getMethod()).thenReturn("POST");

        tokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void publicGetPath_products_allowsWithoutToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/products");
        when(request.getMethod()).thenReturn("GET");

        tokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void authenticatedPath_validToken_setsAttributes() throws ServletException, IOException {
        UserEntity user = new UserEntity(1L, "John", "john@mail.com", "hash", Role.NORMAL);

        when(request.getRequestURI()).thenReturn("/api/cart");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token-123");
        when(userRepository.findByToken("valid-token-123")).thenReturn(user);

        tokenFilter.doFilterInternal(request, response, filterChain);

        verify(request).setAttribute("authenticatedUser", user);
        verify(request).setAttribute("authenticatedUserId", 1L);
        verify(request).setAttribute("authenticatedUserRole", Role.NORMAL);
        verify(request).setAttribute("authenticatedUserName", "John");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void authenticatedPath_noAuthHeader_returns401() throws ServletException, IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(request.getRequestURI()).thenReturn("/api/cart");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn(null);
        when(response.getWriter()).thenReturn(pw);

        tokenFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void authenticatedPath_invalidToken_returns401() throws ServletException, IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(request.getRequestURI()).thenReturn("/api/orders/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        when(userRepository.findByToken("invalid-token")).thenReturn(null);
        when(response.getWriter()).thenReturn(pw);

        tokenFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void optionsRequest_allowsWithoutValidation() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/cart");
        when(request.getMethod()).thenReturn("OPTIONS");

        tokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
