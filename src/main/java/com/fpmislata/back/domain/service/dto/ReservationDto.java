package com.fpmislata.back.domain.service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record ReservationDto(
        Long id,
        @NotNull
        String name,
        @NotNull
        String email,
        @NotNull
        Date reservation_date,
        @NotNull
        Integer phone_number,
        String message
) { }
