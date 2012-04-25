package app.fourthink.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class PasswordHasher {

    private static final SecureRandom RANDOM = new SecureRandom();

    public String hash(String plainText) {
        if (plainText == null || plainText.length() < 6) {
            throw new IllegalArgumentException("password must be at least 6 characters");
        }
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return encode(salt) + "$" + sha256(plainText, salt);
    }

    public boolean matches(String plainText, String stored) {
        if (stored == null || stored.indexOf('$') < 0) {
            return false;
        }
        String[] parts = stored.split("\\$");
        if (parts.length != 2) {
            return false;
        }
        byte[] salt = decode(parts[0]);
        return sha256(plainText, salt).equals(parts[1]);
    }

    private String sha256(String input, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] digest = md.digest(input.getBytes("UTF-8"));
            return encode(digest);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private String encode(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    private byte[] decode(String hex) {
        int len = hex.length();
        byte[] out = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            out[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return out;
    }
}
