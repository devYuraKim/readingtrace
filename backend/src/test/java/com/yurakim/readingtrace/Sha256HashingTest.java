package com.yurakim.readingtrace;

import com.yurakim.readingtrace.auth.util.Sha256Hashing;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class Sha256HashingTest {

    @Test
    void testGenerateSHA256Hash() {
        String originalRefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJyZWFkaW5ndHJhY2UiLCJzdWIiOiJtb3J1eWFAbmF2ZXIuY29tIiwidXNlcklkIjo2LCJpYXQiOjE3NTgyNjMwNjIsImV4cCI6MTc1ODg2MjM4MCwianRpIjoiNWExMDdlMTUtOTc1MC00YWM2LWEyY2UtZThjNDcyYzgxMmEwIn0.KDWurcIC6AvqfCDRytaY5PREoc3TCm7NPLl5fmisOF0";
        String hash = Sha256Hashing.generateSHA256Hash(originalRefreshToken);

        System.out.println("Generated hash: " + hash);

        String expected = "32d2dc723910bd8ae1a16f5e1d14dc0e87bb9c972f8979a41a7f747f5592cfb0";

        assertEquals(expected, hash);
    }
}
