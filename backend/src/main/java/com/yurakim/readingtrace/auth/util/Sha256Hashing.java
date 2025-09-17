package com.yurakim.readingtrace.auth.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256Hashing {

    public static String generateSHA256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] encodedhash = digest.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyDataIntegrity(String originalData, String receivedData) {
        String originalHash = Sha256Hashing.generateSHA256Hash(originalData);
        String receivedHash = Sha256Hashing.generateSHA256Hash(receivedData);
        return originalHash.equals(receivedHash);
    }


}
