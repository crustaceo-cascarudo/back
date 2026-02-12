package com.fpmislata.back.infrastructure.payment.service.impl;

import com.fpmislata.back.domain.Exception.BusinessException;
import com.fpmislata.back.infrastructure.payment.dto.BankCreditCardDto;
import com.fpmislata.back.infrastructure.payment.dto.BankPaymentResponse;
import com.fpmislata.back.infrastructure.payment.request.BankCardPaymentRequest;
import com.fpmislata.back.infrastructure.payment.request.CardPaymentRequest;
import com.fpmislata.back.infrastructure.payment.service.BankPaymentService;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;

public class BankPaymentServiceImpl implements BankPaymentService {

  private final RestTemplate restTemplate;
  private final String bankApiUrl;
  private final String bankApiKey;
  private final String storeRecipientIban;

  public BankPaymentServiceImpl(RestTemplate restTemplate, String bankApiUrl, String bankApiKey,
      String storeRecipientIban) {
    this.restTemplate = restTemplate;
    this.bankApiUrl = bankApiUrl;
    this.bankApiKey = bankApiKey;
    this.storeRecipientIban = storeRecipientIban;
  }

  @Override
  public void processCardPayment(CardPaymentRequest request) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, Integer.parseInt(request.expiryYear()));
    calendar.set(Calendar.MONTH, Integer.parseInt(request.expiryMonth()) - 1);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    Date expirationDate = calendar.getTime();

    BankCreditCardDto creditCard = new BankCreditCardDto(
        Long.parseLong(request.cardNumber()),
        expirationDate,
        Integer.parseInt(request.cvc()),
        request.fullName(),
        request.accountIban());

    BankCardPaymentRequest bankRequest = new BankCardPaymentRequest(
        null,
        null,
        creditCard,
        storeRecipientIban,
        request.amount().floatValue(),
        request.concept());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("X-API-Key", bankApiKey);

    HttpEntity<BankCardPaymentRequest> httpEntity = new HttpEntity<>(bankRequest, headers);

    try {
      ResponseEntity<BankPaymentResponse> response = restTemplate.exchange(
          bankApiUrl + "/api/card-payment",
          HttpMethod.POST,
          httpEntity,
          BankPaymentResponse.class);

      if (!response.getStatusCode().is2xxSuccessful()) {
        throw new BusinessException("Error al procesar el pago con el banco. Código: " + response.getStatusCode());
      }

    } catch (HttpClientErrorException e) {
      String errorBody = e.getResponseBodyAsString();
      throw new BusinessException("Error del banco al procesar el pago: " + errorBody);
    } catch (HttpServerErrorException e) {
      throw new BusinessException("Error interno del servidor del banco. Inténtelo más tarde.");
    } catch (RestClientException e) {
      throw new BusinessException("No se pudo conectar con el servicio del banco: " + e.getMessage());
    }
  }
}
