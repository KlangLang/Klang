package org.klang.core.diagnostic;

import org.klang.core.error.Phase;

public enum DiagnosticCode {
    E001("InvalidCharacter", Phase.LEXICAL),
    E003("InvalidCharacter", Phase.LEXICAL),
    E004("InvalidCharacter", Phase.LEXICAL),

    E002("UnterminatedStringLiteral", Phase.LEXICAL),
    E102("MissingStatementTerminator", Phase.SYNTAX),
    E201("UnknownTypeIdentifier", Phase.SEMANTIC);

    public final String name;
    public final Phase phase;

    DiagnosticCode(String name, Phase phase) {
        this.name = name;
        this.phase = phase;
    }
}
