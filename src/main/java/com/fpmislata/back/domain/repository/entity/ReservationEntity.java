package com.fpmislata.back.domain.repository.entity;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record ReservationEntity(
        Long id,
        String name,
        String email,
        Date reservation_date,
        Integer phone_number,
        String message
) {
}
