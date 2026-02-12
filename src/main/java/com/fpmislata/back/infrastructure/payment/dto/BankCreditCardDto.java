package com.fpmislata.back.infrastructure.payment.dto;

import java.util.Date;

/**
 * DTO que representa la tarjeta de crédito del banco.
 * Coincide con CreditCardDto del proyecto banco.
 */
public record BankCreditCardDto(
    Long cardNumber,
    Date expirationDate,
    int cvc,
    String fullName,
    String accountIban
) {
}
