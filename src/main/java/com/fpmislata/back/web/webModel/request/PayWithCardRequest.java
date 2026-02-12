package com.fpmislata.back.web.webModel.request;

import java.util.Date;

public record PayWithCardRequest(
    Long cardNumber,
    Date expirationDate,
    int cvc,
    String fullName,
    String accountIban,
    String address
) {
}
