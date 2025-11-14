package org.klang.core.errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Diagnostic {
    public final DiagnosticType type;
    public final String message;
    public final Span primarySpan;
    private final List<Span> secondarySpans;
    private final List<Note> notes;

    public Diagnostic(DiagnosticType type, String message, Span primarySpan) {
        this.type = type;
        this.message = message;
        this.primarySpan = primarySpan;
        this.secondarySpans = new ArrayList<>();
        this.notes = new ArrayList<>();
    }

    public Diagnostic addSecondarySpan(Span s) {
        this.secondarySpans.add(s);
        return this;
    }

    public Diagnostic addNote(Note n) {
        this.notes.add(n);
        return this;
    }

    public List<Span> secondarySpans() {
        return Collections.unmodifiableList(secondarySpans);
    }

    public List<Note> notes() {
        return Collections.unmodifiableList(notes);
    }
}
