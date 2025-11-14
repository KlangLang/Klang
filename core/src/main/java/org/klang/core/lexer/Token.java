package org.klang.core.lexer;

public class Token {
    TokenType type;
    String value;
    // int[] position;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        String saida = type + "(" + value + ")";

        return saida;
    }
}
