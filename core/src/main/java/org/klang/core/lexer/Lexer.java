package org.klang.core.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.klang.core.diagnostic.DiagnosticCode;
import org.klang.core.error.LexicalException;
import org.klang.core.error.SourceLocation;
import org.klang.core.error.SourceManager;

public class Lexer {

    List<Token> tokens = new ArrayList<>();

    int position = 0;
    int line = 1;
    int column = 0;

    String source;
    String filePath;

    private final SourceManager sourceManager;

    HashMap<String, TokenType> tokensTypeByString = new HashMap<>();
    HashMap<Character, TokenType> tokensTypeByChar = new HashMap<>();

    public Lexer(String source, String filePath) {
        this.source = source;
        this.filePath = filePath;
        this.sourceManager = new SourceManager(source);

        initialzerhashMapTokensTypes();
    }

    public List<Token> tokenizeSourceCode() {

        while (!isAtEnd()) {

            char c = peek();

            if (Character.isWhitespace(c)) {
                advance();

                if (c == '\n') {
                    line++;
                    column = 0;
                }

                continue;
            }

            if (c == '"') {
                int startLine = this.line;
                int startColumn = this.position; 

                advance();

                String content = readString(startLine, startColumn);
                tokens.add(new Token(
                        TokenType.STRING_LITERAL,
                        content,
                        line,
                        position));

                continue;
            }

            if (c == '\'') {
                advance();

                String content = readCharacter();
                tokens.add(new Token(
                        TokenType.CHARACTER_LITERAL,
                        content,
                        line,
                        position));

                continue;
            }

            if (Character.isLetter(c) || c == '_' || c == '$') {

                if (c == '$' && !(Character.isLetter(peekNext()) || peekNext() == '_')) {
                    lexicalError(
                            DiagnosticCode.E001,
                            "The character '$' cannot start an identifier alone.",
                            "Identifiers starting with '$' must contain a letter or underscore.",
                            "integer $variableName = 10;");
                }

                String ident = readIdentifier();
                TokenType tokenType = tokensTypeByString.getOrDefault(ident, TokenType.IDENTIFIER);

                if (tokenType == TokenType.IDENTIFIER) {
                    tokens.add(new Token(tokenType, ident, line, position));
                } else {
                    tokens.add(new Token(tokenType));
                }

                continue;
            }

            if (Character.isDigit(c)) {
                String num = readNumber();
                tokens.add(new Token(TokenType.NUMBER, num, line, position));
                continue;
            }

            if (peek() == '/' && peekNext() == '/') {
                advance();
                advance();

                while (!isAtEnd() && peek() != '\n') {
                    advance();
                }

                continue;
            }

            if (peek() == '/' && peekNext() == '*') {
                advance();
                advance();

                while (!isAtEnd()) {

                    if (peek() == '*' && peekNext() == '/') {
                        advance();
                        advance();
                        break;
                    }

                    if (peek() == '\n') {
                        line++;
                        column = 0;
                    }

                    advance();
                }

                continue;
            }

            TokenType tokenType = tokensTypeByChar.get(c);

            switch (c) {
                case '@':
                    advance();

                    tokens.add(new Token(TokenType.AT));

                    continue;

                case '=':
                    advance();

                    if (match('=')) {
                        tokens.add(new Token(TokenType.DOUBLEEQUAL));
                    } else {
                        tokens.add(new Token(tokenType));
                    }

                    continue;

                case '+':
                    advance();

                    if (match('+')) {
                        tokens.add(new Token(TokenType.INCREMENT));
                    } else {
                        tokens.add(new Token(tokenType));
                    }

                    continue;

                case '.':
                    advance();

                    tokens.add(new Token(TokenType.DOT));

                    continue;

                case '-':
                    advance();

                    if (match('-')) {
                        tokens.add(new Token(TokenType.DECREMENT));
                    } else if (match('>')) {
                        tokens.add(new Token(TokenType.ARROW));
                    } else {
                        tokens.add(new Token(tokenType));
                    }

                    continue;

                case '*':
                    advance();

                    if (match('*')) {
                        tokens.add(new Token(TokenType.POWER));
                    } else {
                        tokens.add(new Token(tokenType));
                    }

                    continue;

                case '>':
                    advance();

                    if (match('=')) {
                        tokens.add(new Token(TokenType.GTE));
                    } else {
                        tokens.add(new Token(tokenType));
                    }

                    continue;

                case '<':
                    advance();

                    if (match('=')) {
                        tokens.add(new Token(TokenType.LTE));
                    } else {
                        tokens.add(new Token(tokenType));
                    }

                    continue;

                case '!':
                    advance();

                    if (match('=')) {
                        tokens.add(new Token(TokenType.NOTEQUAL));
                    } else {
                        tokens.add(new Token(TokenType.BANG));
                    }

                    continue;

                case '&':
                    advance();

                    if (match('&')) {
                        tokens.add(new Token(TokenType.AND));

                        continue;
                    }

                    lexicalError(
                            DiagnosticCode.E001,
                            "Character '&' is not valid alone.",
                            "Use '&&' for logical AND.",
                            "if (firstCondition && secondCondition) {\n\tprintln(\"The first and second conditions are in agreement.\")\n  }");
                    break;

                case '|':
                    advance();

                    if (match('|')) {
                        tokens.add(new Token(TokenType.OR));

                        continue;
                    }

                    lexicalError(
                            DiagnosticCode.E001,
                            "Character '|' is not valid alone.",
                            "Use '||' for logical OR.",
                            "if (firstCondition && secondCondition) {\n\tprintln(\"Either the first or second condition is met.\")\n  }");
                    break;
            }

            if (tokenType == null) {
                lexicalError(
                        DiagnosticCode.E001,
                        "Character '" + c + "' is not valid in Klang.",
                        "Remove or replace it.",
                        null);
            }

            tokens.add(new Token(tokenType));
            advance();
        }

        tokens.add(new Token(TokenType.EOF));
        return tokens;
    }

    private String readString(int startLine, int startColumn) {

        StringBuilder s = new StringBuilder();

        while (!isAtEnd()) {

            char c = advance();

            if (c == '"') {
                return s.toString();
            }

            if (c == '\n') {
                lexicalError(
                        DiagnosticCode.E002,
                        "String literal cannot span multiple lines.",
                        "Close the string before the line break.",
                        "\"" + s.toString().strip() + "\""

                );
            }

            if (c == '\\') {

                if (isAtEnd()) {
                    lexicalError(
                            DiagnosticCode.E002,
                            "Unclosed string literal.",
                            "Add closing quote.",
                            "\"" + s.toString().strip() + "\"");
                }

                char escaped = advance();

                if (escaped == 'n') {
                    s.append('\n');
                } else if (escaped == 't') {
                    s.append('\t');
                } else if (escaped == '"') {
                    s.append('"');
                } else if (escaped == '\\') {
                    s.append('\\');
                } else {
                    lexicalError(
                            DiagnosticCode.E003,
                            "Invalid escape sequence: \\" + escaped,
                            "Use valid escapes like \\n, \\t, \\\".",
                            "\"" + s.toString().strip() + "\""

                    );
                }

                continue;
            }

            s.append(c);
        }

        lexicalError(
                DiagnosticCode.E002,
                "Unclosed string literal.",
                "Add closing quote.",
                "\"" + s.toString().strip() + "\""

        );

        return null;
    }

    private String readCharacter() {

        if (isAtEnd()) {
            lexicalError(
                    DiagnosticCode.E004,
                    "Unclosed character literal.",
                    "Add closing '.",
                    "'a'");
        }

        char c = advance();
        String value;

        if (c == '\\') {

            if (isAtEnd()) {
                lexicalError(
                        DiagnosticCode.E004,
                        "Unclosed character literal.",
                        "Add closing '.",
                        "'\\n'");
            }

            char escaped = advance();

            if (escaped == 'n') {
                value = "\n";
            } else if (escaped == 't') {
                value = "\t";
            } else if (escaped == '\'') {
                value = "'";
            } else if (escaped == '\\') {
                value = "\\";
            } else {
                lexicalError(
                        DiagnosticCode.E003,
                        "Invalid escape in character literal: \\" + escaped,
                        "Use valid escapes.",
                        "'\\n'");
                return null;
            }

        } else {
            value = String.valueOf(c);
        }

        if (isAtEnd() || peek() != '\'') {
            lexicalError(
                    DiagnosticCode.E004,
                    "Unclosed character literal.",
                    "Add closing '.",
                    "'a'");
        }

        advance();
        return value;
    }

    private String readIdentifier() {

        StringBuilder s = new StringBuilder();
        s.append(advance());

        while (Character.isLetterOrDigit(peek()) || peek() == '_') {
            s.append(advance());
        }

        return s.toString();
    }

    private String readNumber() {

        StringBuilder s = new StringBuilder();

        while (Character.isDigit(peek())) {
            s.append(advance());
        }

        if (peek() == '.' && Character.isDigit(peekNext())) {
            s.append(advance());

            while (Character.isDigit(peek())) {
                s.append(advance());
            }
        }

        if (Character.isLetter(peek())) {
            lexicalError(
                    DiagnosticCode.E001,
                    "Invalid numeric literal.",
                    "Numbers cannot be followed by letters.",
                    "123");
        }

        return s.toString();
    }

    private boolean isAtEnd() {
        return position >= source.length();
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';

        }

        return source.charAt(position);
    }

    private char peekNext() {
        if (position + 1 >= source.length()) {
            return '\0';

        }

        return source.charAt(position + 1);
    }

    private char advance() {
        char c = peek();
        position++;
        column++;
        return c;
    }

    private boolean match(char expected) {

        if (isAtEnd()) {
            return false;
        }

        if (peek() != expected) {
            return false;
        }

        advance();
        return true;
    }

    private void lexicalError(
            DiagnosticCode code,
            String cause,
            String fix,
            String example) {

        throw new LexicalException(
                code,
                new SourceLocation(filePath, line, Math.max(column - 1, 0)),
                sourceManager.getContextLines(line, 2),
                cause,
                fix,
                example,
                null);
    }

    private void initialzerhashMapTokensTypes() {
        // Keywords
        tokensTypeByString.put("return", TokenType.RETURN);
        tokensTypeByString.put("if", TokenType.IF);
        tokensTypeByString.put("otherwise", TokenType.OTHERWISE);
        tokensTypeByString.put("afterall", TokenType.AFTERALL);
        tokensTypeByString.put("for", TokenType.FOR);
        tokensTypeByString.put("while", TokenType.WHILE);
        tokensTypeByString.put("break", TokenType.BREAK);
        tokensTypeByString.put("continue", TokenType.CONTINUE);
        tokensTypeByString.put("public", TokenType.PUBLIC);
        tokensTypeByString.put("internal", TokenType.INTERNAL);
        tokensTypeByString.put("protected", TokenType.PROTECTED);
        tokensTypeByString.put("static", TokenType.STATIC);
        tokensTypeByString.put("true", TokenType.TRUE);
        tokensTypeByString.put("false", TokenType.FALSE);
        tokensTypeByString.put("integer", TokenType.INTEGER);
        tokensTypeByString.put("try", TokenType.TRY);
        tokensTypeByString.put("catch", TokenType.CATCH);
        tokensTypeByString.put("double", TokenType.DOUBLE);
        tokensTypeByString.put("boolean", TokenType.BOOLEAN);
        tokensTypeByString.put("character", TokenType.CHARACTER_TYPE);
        tokensTypeByString.put("void", TokenType.VOID);
        tokensTypeByString.put("null", TokenType.NULL);
        tokensTypeByString.put("new", TokenType.NEW);
        tokensTypeByString.put("Use", TokenType.USE);

        // References
        tokensTypeByString.put("String", TokenType.STRING_TYPE);

        // Single-Characters
        tokensTypeByChar.put('(', TokenType.LPAREN);
        tokensTypeByChar.put(')', TokenType.RPAREN);
        tokensTypeByChar.put('{', TokenType.LBRACE);
        tokensTypeByChar.put('}', TokenType.RBRACE);
        tokensTypeByChar.put('[', TokenType.LBRACKET);
        tokensTypeByChar.put(']', TokenType.RBRACKET);
        tokensTypeByChar.put(',', TokenType.COMMA);
        tokensTypeByChar.put(';', TokenType.SEMICOLON);
        tokensTypeByChar.put(':', TokenType.COLON);
        tokensTypeByChar.put('.', TokenType.DOT);
        tokensTypeByChar.put('+', TokenType.PLUS);
        tokensTypeByChar.put('-', TokenType.MINUS);
        tokensTypeByChar.put('*', TokenType.MULTIPLY);
        tokensTypeByChar.put('/', TokenType.DIVISION);
        tokensTypeByChar.put('%', TokenType.REMAINDER);
        tokensTypeByChar.put('=', TokenType.ASSIGNMENT);
        tokensTypeByChar.put('<', TokenType.LT);
        tokensTypeByChar.put('>', TokenType.GT);
        tokensTypeByChar.put('!', TokenType.BANG);
        // Specials
        tokensTypeByChar.put('@', TokenType.AT);
    }
}
