package com.fpmislata.back.domain.service;

public interface PasswordEncoderService {
    String encode(String rawPassword);
    boolean verify(String rawPassword, String encodedPassword);
}
