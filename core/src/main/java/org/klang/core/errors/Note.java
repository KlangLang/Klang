package org.klang.core.errors;

public final class Note {
    public final String message;
    public final Span span; // pode ser null

    public Note(String message) {
        this(message, null);
    }

    public Note(String message, Span span) {
        this.message = message;
        this.span = span;
    }
}
