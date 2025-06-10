package com.desmond.gadgetstore.common.utils;

import java.util.Random;

public final class Helper {
    private Helper() {
    }

    ;

    public static String generateProductCode(String brandName, String categoryName, long count) {
        String first = brandName.substring(0, 1).toUpperCase();
        String sec = categoryName.substring(0, 1).toUpperCase();
        return first + sec + count;
    }

    public static String generateRandom(int length) {
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars
                    .length())));
        }
        return sb.toString();
    }

    public static String getEmailVerificationKey(String token) {
        return "EMAIL-VERIFICATION::".concat(token);
    }
}
