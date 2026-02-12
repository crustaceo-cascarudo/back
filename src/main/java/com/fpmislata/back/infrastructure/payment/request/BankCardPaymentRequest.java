package com.fpmislata.back.infrastructure.payment.request;

import com.fpmislata.back.infrastructure.payment.dto.BankCreditCardDto;
import com.fpmislata.back.infrastructure.payment.dto.BankUserDto;

public record BankCardPaymentRequest(
    BankUserDto user,
    String apiToken,
    BankCreditCardDto originCreditCard,
    String recipientIban,
    float amount,
    String concept
) {
}
