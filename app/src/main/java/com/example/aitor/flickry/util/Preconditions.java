package com.example.aitor.flickry.util;

public final class Preconditions {
    private Preconditions() {
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }

        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @param message   exception message
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference, String message) {
        if (reference == null) {
            throw new NullPointerException(message);
        }

        return reference;
    }


    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }
}
