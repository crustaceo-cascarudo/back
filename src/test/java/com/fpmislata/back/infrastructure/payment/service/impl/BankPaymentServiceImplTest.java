package com.fpmislata.back.infrastructure.payment.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fpmislata.back.domain.Exception.BusinessException;
import com.fpmislata.back.infrastructure.payment.dto.BankPaymentResponse;
import com.fpmislata.back.infrastructure.payment.request.CardPaymentRequest;

class BankPaymentServiceImplTest {

    private RestTemplate restTemplate;
    private BankPaymentServiceImpl bankPaymentService;

    private static final String BANK_API_URL = "http://localhost:8081";
    private static final String BANK_API_KEY = "test-api-key";
    private static final String STORE_IBAN = "ES1234567890";

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        bankPaymentService = new BankPaymentServiceImpl(restTemplate, BANK_API_URL, BANK_API_KEY, STORE_IBAN);
    }

    private CardPaymentRequest buildRequest() {
        return new CardPaymentRequest(
                "4111111111111111", "12", "2030", "123",
                "Test User", "ES9876543210",
                BigDecimal.valueOf(50), "Crustaceo-cascarudo");
    }

    @Test
    void processCardPayment_success_doesNotThrow() {
        CardPaymentRequest request = buildRequest();
        BankPaymentResponse bankResponse = mock(BankPaymentResponse.class);
        ResponseEntity<BankPaymentResponse> responseEntity = new ResponseEntity<>(bankResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(BANK_API_URL + "/api/card-payment"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(BankPaymentResponse.class)
        )).thenReturn(responseEntity);

        assertDoesNotThrow(() -> bankPaymentService.processCardPayment(request));
    }

    @Test
    void processCardPayment_clientError_throwsBusinessException() {
        CardPaymentRequest request = buildRequest();

        when(restTemplate.exchange(
                eq(BANK_API_URL + "/api/card-payment"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(BankPaymentResponse.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> bankPaymentService.processCardPayment(request));
        assertTrue(ex.getMessage().contains("Error del banco"));
    }

    @Test
    void processCardPayment_connectionError_throwsBusinessException() {
        CardPaymentRequest request = buildRequest();

        when(restTemplate.exchange(
                eq(BANK_API_URL + "/api/card-payment"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(BankPaymentResponse.class)
        )).thenThrow(new RestClientException("Connection refused"));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> bankPaymentService.processCardPayment(request));
        assertTrue(ex.getMessage().contains("conectar"));
    }

    @Test
    void processCardPayment_non2xxResponse_throwsBusinessException() {
        CardPaymentRequest request = buildRequest();
        BankPaymentResponse bankResponse = mock(BankPaymentResponse.class);
        ResponseEntity<BankPaymentResponse> responseEntity = new ResponseEntity<>(bankResponse,
                HttpStatus.SERVICE_UNAVAILABLE);

        when(restTemplate.exchange(
                eq(BANK_API_URL + "/api/card-payment"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(BankPaymentResponse.class)
        )).thenReturn(responseEntity);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> bankPaymentService.processCardPayment(request));
        assertTrue(ex.getMessage().contains("Error al procesar"));
    }
}
