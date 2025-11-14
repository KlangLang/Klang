package org.klang.core.errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Colecionador de diagnostics. Utilize-o durante parsing/semântica para
 * acumular erros
 * e decidir se o processo deve prosseguir ou abortar ao fim.
 */
public final class DiagnosticCollector {
    private final List<Diagnostic> diagnostics = new ArrayList<>();

    public void report(Diagnostic d) {
        diagnostics.add(d);
    }

    public boolean hasErrors() {
        return diagnostics.stream().anyMatch(d -> d.type == DiagnosticType.LEXICAL
                || d.type == DiagnosticType.SYNTAX
                || d.type == DiagnosticType.SEMANTIC
                || d.type == DiagnosticType.TYPE);
    }

    public List<Diagnostic> all() {
        return Collections.unmodifiableList(diagnostics);
    }

    public void throwIfErrors() {
        if (hasErrors()) {
            // lança o primeiro como exceção — a CLI deve imprimir todos usando
            // collector.all()
            throw new DiagnosticException(diagnostics.get(0));
        }
    }
}
