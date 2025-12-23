package org.klang.core.error;

import org.klang.core.diagnostic.DiagnosticCode;

public abstract class KException extends RuntimeException {

    protected final DiagnosticCode code;
    protected final SourceLocation location;
    protected final String[] contextLines;
    protected final String cause;
    protected final String fix;
    protected final String example;
    protected final String note;

    protected KException(
        DiagnosticCode code,
        SourceLocation location,
        String[] contextLines,
        String cause,
        String fix,
        String example,
        String note
    ) {
        super(code.name);
        this.code = code;
        this.location = location;
        this.contextLines = contextLines;
        this.cause = cause;
        this.fix = fix;
        this.example = example;
        this.note = note;
    }

    @Override
    public final String getMessage() {
        return format();
    }

    @Override
    public String toString() {
        // Isso garante que o toString() também use seu formato customizado
        return format();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this; 
    }

    public abstract String format();
}
