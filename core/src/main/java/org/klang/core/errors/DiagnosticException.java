package org.klang.core.errors;

/**
 * Exception que carrega o Diagnostic. Em geral é lançada quando não há como
 * recuperar.
 */
public final class DiagnosticException extends RuntimeException {
    public final Diagnostic diagnostic;

    public DiagnosticException(Diagnostic diagnostic) {
        super(diagnostic.message);
        this.diagnostic = diagnostic;
    }
}
