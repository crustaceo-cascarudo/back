package com.fpmislata.back.infrastructure;

import com.fpmislata.back.domain.service.PasswordEncoderService;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordEncoderImpl implements PasswordEncoderService {
    @Override
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray());
    }

    @Override
    public boolean verify(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}
