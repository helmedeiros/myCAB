package app.fourthink.model;

import java.util.regex.Pattern;

public final class Phone {

    private static final Pattern PATTERN = Pattern.compile("^[+\\d(][\\d\\s().+-]+$");
    private static final Pattern DIGITS = Pattern.compile("\\d");

    private final String value;

    public Phone(String value) {
        if (value == null) {
            throw new IllegalArgumentException("phone must not be null");
        }
        String trimmed = value.trim();
        if (!PATTERN.matcher(trimmed).matches() || digitCount(trimmed) < 8) {
            throw new IllegalArgumentException("invalid phone number: " + value);
        }
        this.value = trimmed;
    }

    private static int digitCount(String input) {
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) count++;
        }
        return count;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Phone)) return false;
        return value.equals(((Phone) other).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
