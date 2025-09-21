package com.yurakim.readingtrace.auth;

import com.yurakim.readingtrace.auth.util.Sha256Hashing;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class Sha256HashingTest {

    @Test
    void testGenerateSHA256Hash() {
        String originalRefreshToken = "";
        String hash = Sha256Hashing.generateSHA256Hash(originalRefreshToken);

        System.out.println("Generated hash: " + hash);

        String expected = "";

        assertEquals(expected, hash);
    }
}
