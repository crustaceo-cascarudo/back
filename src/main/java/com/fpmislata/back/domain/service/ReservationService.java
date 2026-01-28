package com.fpmislata.back.domain.service;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.service.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    Page<ReservationDto> findAll(int page, int size);
    List<ReservationDto> findByName(String name);
    List<ReservationDto> findByEmail(String email);
    ReservationDto getById(Long id);
    ReservationDto create(ReservationDto productDto);
    ReservationDto update(ReservationDto productDto);
    void deleteById(Long id);
}
