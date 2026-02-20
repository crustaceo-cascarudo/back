package com.fpmislata.back.infrastructure;

import com.fpmislata.back.domain.service.PasswordEncoderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderImplTest {

    private PasswordEncoderService passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new PasswordEncoderImpl();
    }

    @Test
    void testEncodePasswordIsNotNullAndDifferentFromRaw() {
        String rawPassword = "mySecurePassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2y$") || encodedPassword.startsWith("$2b$"));
        assertTrue(encodedPassword.length() > 50);
    }

    @Test
    void testEncodeProducesDifferentHashesForSamePassword() {
        String rawPassword = "anotherPassword";
        String encodedPassword1 = passwordEncoder.encode(rawPassword);
        String encodedPassword2 = passwordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword1);
        assertNotNull(encodedPassword2);
        assertNotEquals(encodedPassword1, encodedPassword2);
    }

    @Test
    void testVerifyReturnsTrueForCorrectPassword() {
        String rawPassword = "correctPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.verify(rawPassword, encodedPassword));
    }

    @Test
    void testVerifyReturnsFalseForIncorrectPassword() {
        String rawPassword = "correctPassword";
        String wrongPassword = "incorrectPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertFalse(passwordEncoder.verify(wrongPassword, encodedPassword));
    }

    @Test
    void testVerifyReturnsFalseForNullRawPassword() {
        String rawPassword = "somePassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertFalse(passwordEncoder.verify(null, encodedPassword));
    }

    @Test
    void testVerifyReturnsFalseForNullEncodedPassword() {
        assertFalse(passwordEncoder.verify("anyPassword", null));
    }

    @Test
    void testVerifyReturnsFalseForEmptyRawPassword() {
        String rawPassword = "somePassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertFalse(passwordEncoder.verify("", encodedPassword));
    }

    @Test
    void testVerifyReturnsFalseForEmptyEncodedPassword() {
        assertFalse(passwordEncoder.verify("anyPassword", ""));
    }
}