package com.fpmislata.back.infrastructure.payment.service;

import com.fpmislata.back.infrastructure.payment.request.CardPaymentRequest;

public interface BankPaymentService {
    void processCardPayment(CardPaymentRequest request);
}
