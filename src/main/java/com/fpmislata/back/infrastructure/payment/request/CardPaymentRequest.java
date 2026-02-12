package com.fpmislata.back.infrastructure.payment.request;

import java.math.BigDecimal;

public record CardPaymentRequest(
    String cardNumber,
    String expiryMonth,
    String expiryYear,
    String cvc,
    String fullName,
    String accountIban,
    BigDecimal amount,
    String concept
) {
}
