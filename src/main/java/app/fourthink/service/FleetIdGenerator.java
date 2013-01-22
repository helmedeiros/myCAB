package app.fourthink.service;

import app.fourthink.persistence.CabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class FleetIdGenerator {

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int LENGTH = 4;
    private static final int MAX_ATTEMPTS = 16;

    private final SecureRandom random = new SecureRandom();
    private CabRepository cabs;

    public FleetIdGenerator() {
    }

    @Autowired
    public FleetIdGenerator(CabRepository cabs) {
        this.cabs = cabs;
    }

    public String next() {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            String candidate = generate();
            if (cabs == null || cabs.findByFleetId(candidate) == null) {
                return candidate;
            }
        }
        throw new IllegalStateException("exhausted fleet id attempts");
    }

    private String generate() {
        char[] out = new char[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            out[i] = ALPHABET.charAt(random.nextInt(ALPHABET.length()));
        }
        return new String(out);
    }
}
