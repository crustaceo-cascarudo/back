package com.fpmislata.back.web.webModel.request;

public record PayWithCardRequest(
    String cardNumber,
    String expiryMonth,
    String expiryYear,
    String cvc,
    String fullName,
    String accountIban,
    String address
) {
}
