package com.fpmislata.back.infrastructure.payment.dto;

import java.util.Date;

/**
 * DTO que representa la respuesta del banco tras procesar un pago.
 * Coincide con BankMovementDto del proyecto banco.
 */
public record BankPaymentResponse(
    Long id,
    String movementType,
    String paymentMethod,
    String originAccountIban,
    Long originCreditCardNumber,
    String recipientAccountIban,
    Date movementDate,
    float amount,
    String concept
) {
}
