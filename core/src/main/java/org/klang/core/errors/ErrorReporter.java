package org.klang.core.errors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Reporta diagnostics no estilo Rust: mensagem, arrow location, linha e
 * pointer.
 * Simples, sem dependências externas. Usa ANSI escapes se o terminal suportar.
 */
public final class ErrorReporter {

    // ANSI codes
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    private ErrorReporter() {
    }

    public static void report(Diagnostic d) {
        try {
            printDiagnostic(d);
        } catch (Exception e) {
            // fallback simples
            System.err.println(d.type + " ERROR: " + d.message);
        }
    }

    public static void reportAll(List<Diagnostic> list) {
        for (Diagnostic d : list) {
            report(d);
        }
    }

    private static String colorFor(DiagnosticType type) {
        return switch (type) {
            case LEXICAL -> RED;
            case SYNTAX -> YELLOW;
            case SEMANTIC -> MAGENTA;
            case TYPE -> CYAN;
            case WARNING -> YELLOW;
            case INFO -> CYAN;
        };
    }

    private static void printDiagnostic(Diagnostic d) throws IOException {
        Span span = d.primarySpan;
        String color = colorFor(d.type);
        System.err.println();
        System.err.println(BOLD + color + d.type + " ERROR" + RESET + ": " + d.message);
        if (span != null) {
            System.err.println("--> " + span.toString());
            // lê a linha do arquivo
            List<String> lines = Files.readAllLines(Path.of(span.fileName));
            int idx = span.startLine - 1;
            String lineText = idx >= 0 && idx < lines.size() ? lines.get(idx) : "";
            // printa contexto
            System.err.println();
            String lineNum = String.valueOf(span.startLine);
            System.err.printf(" %s | %s%n", lineNum, lineText);

            // calcular ponteiro (account for column start at 1)
            int pointerPos = span.startColumn - 1;
            int offset = lineNum.length() + 3 + pointerPos; // " {line} | "
            String pointer = " ".repeat(Math.max(0, offset)) + "^";
            System.err.println(pointer);

            // imprime secondary spans como notas (breve)
            for (Span s : d.secondarySpans()) {
                System.err.println(" note: referenced at " + s.toString());
            }
        }

        for (Note n : d.notes()) {
            if (n.span != null) {
                System.err.println(" note: " + n.message + " --> " + n.span.toString());
            } else {
                System.err.println(" note: " + n.message);
            }
        }
        System.err.println();
    }
}
