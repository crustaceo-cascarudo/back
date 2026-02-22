package com.fpmislata.back.web.filter;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fpmislata.back.domain.enumerado.Role;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class AdminRoleFilterTest {

    private AdminRoleFilter adminRoleFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        adminRoleFilter = new AdminRoleFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void adminWriteRoute_withAdminRole_allows() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/products");
        when(request.getMethod()).thenReturn("POST");
        when(request.getAttribute("authenticatedUserRole")).thenReturn(Role.ADMIN);

        adminRoleFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void adminWriteRoute_withNormalRole_returns403() throws ServletException, IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(request.getRequestURI()).thenReturn("/api/products");
        when(request.getMethod()).thenReturn("POST");
        when(request.getAttribute("authenticatedUserRole")).thenReturn(Role.NORMAL);
        when(response.getWriter()).thenReturn(pw);

        adminRoleFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void adminWriteRoute_noRole_returns403() throws ServletException, IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(request.getRequestURI()).thenReturn("/api/categories");
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getAttribute("authenticatedUserRole")).thenReturn(null);
        when(response.getWriter()).thenReturn(pw);

        adminRoleFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void exceptionPath_register_allowsAnyRole() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/users/register");
        when(request.getMethod()).thenReturn("POST");

        adminRoleFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void exceptionPath_login_allowsAnyRole() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/users/login");
        when(request.getMethod()).thenReturn("POST");

        adminRoleFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void getOnUsersWithAdmin_allows() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/users");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("authenticatedUserRole")).thenReturn(Role.ADMIN);

        adminRoleFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void getOnUsersWithNormalRole_returns403() throws ServletException, IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(request.getRequestURI()).thenReturn("/api/users");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("authenticatedUserRole")).thenReturn(Role.NORMAL);
        when(response.getWriter()).thenReturn(pw);

        adminRoleFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void optionsRequest_allowsWithoutValidation() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/products");
        when(request.getMethod()).thenReturn("OPTIONS");

        adminRoleFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
