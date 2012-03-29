package app.fourthink.model;

import java.util.regex.Pattern;

public final class Plate {

    private static final Pattern PATTERN = Pattern.compile("^[A-Z]{3}-?\\d{4}$");

    private final String value;

    public Plate(String value) {
        if (value == null) {
            throw new IllegalArgumentException("plate must not be null");
        }
        String normalized = value.toUpperCase().replace("-", "");
        if (!PATTERN.matcher(value.toUpperCase()).matches()) {
            throw new IllegalArgumentException("plate must look like ABC-1234");
        }
        this.value = normalized.substring(0, 3) + "-" + normalized.substring(3);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Plate)) return false;
        return value.equals(((Plate) other).value);
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
