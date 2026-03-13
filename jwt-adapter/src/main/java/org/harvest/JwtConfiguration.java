package org.harvest;

import java.time.Clock;
import java.time.Duration;

public record JwtConfiguration(
        String signingKey,
        Duration defaultTtl,
        boolean enableCompression,
        Clock clock
) {

    /**
     * Creates a new JWT configuration with sensible defaults.
     *
     * @param signingKey the secret key for signing JWTs (minimum 256 bits recommended)
     * @param clock      the clock for time operations
     * @return configured JwtConfiguration
     */
    public static JwtConfiguration create(String signingKey, Clock clock) {
        return new JwtConfiguration(
                signingKey,
                Duration.ofDays(1),
                true,
                clock
        );
    }

    /**
     * Creates a new JWT configuration with custom TTL.
     *
     * @param signingKey the secret key for signing JWTs
     * @param defaultTtl the default time-to-live for sessions
     * @param clock      the clock for time operations
     * @return configured JwtConfiguration
     */
    public static JwtConfiguration create(String signingKey, Duration defaultTtl, Clock clock) {
        return new JwtConfiguration(
                signingKey,
                defaultTtl,
                true,
                clock
        );
    }

    /**
     * Validates the configuration parameters.
     *
     * @throws IllegalArgumentException if configuration is invalid
     */
    public void validate() {
        if (signingKey == null || signingKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Signing key cannot be null or empty");
        }

        if (signingKey.length() < 32) {
            throw new IllegalArgumentException("Signing key should be at least 256 bits (32 characters) for security");
        }

        if (defaultTtl == null || defaultTtl.isNegative() || defaultTtl.isZero()) {
            throw new IllegalArgumentException("Default TTL must be positive");
        }

        if (clock == null) {
            throw new IllegalArgumentException("Clock cannot be null");
        }
    }
}