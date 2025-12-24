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
    private StringBuilder stringBuilder = new StringBuilder();

    HashMap<String, TokenType> tokensTypeByString = new HashMap<>();
    HashMap<Character, TokenType> tokensTypeByChar = new HashMap<>();

    public Lexer(String source, String filePath) {
        this.source = source;
        this.filePath = filePath;
        this.sourceManager = new SourceManager(source);

        initialzerhashMapTokensTypes();
    }

    public List<Token> tokenizeSourceCode() {
        this.stringBuilder.setLength(0);
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
                    String example = "integer $variableName = 10;"; 

                    lexicalError(
                            DiagnosticCode.E001,
                            "The character '$' cannot start an identifier alone.",
                            "Identifiers starting with '$' must contain a letter or underscore.",
                            example, null, 1);
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
            this.stringBuilder.setLength(0);
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
                    this.stringBuilder.append("&");

                    while (peek() == '&') {
                        this.stringBuilder.append(advance());
                    }

                    lexicalError(
                            DiagnosticCode.E001,
                            "This '"+  this.stringBuilder.toString() + "' is not valid.",
                            "Use 'and' for logical AND.",
                            "if (firstCondition and secondCondition) {\n\tprintln(\"The first and second conditions are in agreement.\")\n  }",
                        null ,this.stringBuilder.length());
                    break;

                case '|':
                    advance();
                    this.stringBuilder.append("|");

                    while (peek() == '|') {
                        this.stringBuilder.append(advance());
                    }

                    lexicalError(
                            DiagnosticCode.E001,
                            "This '"+  this.stringBuilder.toString() + "' is not valid.",
                            "Use 'or' for logical OR.",
                            "if (firstCondition or secondCondition) {\n\tprintln(\"The first and second conditions are in agreement.\")\n  }",
                        null, this.stringBuilder.length());
                    break;
            }

            if (tokenType == null) {
                lexicalError(
                        DiagnosticCode.E001,
                        "Character '" + c + "' is not valid in Klang.",
                        "Remove or replace it.",
                        null, null,
                        1
                    );
            }

            tokens.add(new Token(tokenType));
            advance();
        }

        tokens.add(new Token(TokenType.EOF));
        return tokens;
    }

    private String readString(int startLine, int startColumn) {
        this.stringBuilder.setLength(0);
        String example = "\"" + this.stringBuilder.toString().strip() + "\"";

        while (!isAtEnd()) {
            char c = advance();
            if (c == '"') {
                return this.stringBuilder.toString();
            }

            if (c == '\n') {
                int errorLength = this.stringBuilder.length();

                lexicalError(
                        DiagnosticCode.E002,
                        "String literal cannot span multiple lines.",
                        "Close the string before the line break.",
                        example, null, errorLength

                );
            }

            if (c == '\\') {

                if (isAtEnd()) {
                    int errorLength = this.stringBuilder.length();

                    lexicalError(
                            DiagnosticCode.E002,
                            "Unclosed string literal.",
                            "Add closing quote.",
                            example, null, errorLength);
                }

                char escaped = advance();

                if (escaped == 'n') {
                    this.stringBuilder.append('\n');
                } else if (escaped == 't') {
                    this.stringBuilder.append('\t');
                } else if (escaped == '"') {
                    this.stringBuilder.append('"');
                } else if (escaped == '\\') {
                    this.stringBuilder.append('\\');
                } else {
                    int errorLength = this.stringBuilder.length();

                    lexicalError(
                            DiagnosticCode.E004,
                            "Invalid escape sequence: \\" + escaped,
                            "Use valid escapes like \\n, \\t, \\\".", example, null, errorLength

                    );
                }

                continue;
            }

            this.stringBuilder.append(c);
        }

        int errorLength = this.stringBuilder.length();
        lexicalError(
                DiagnosticCode.E002,
                "Unclosed string literal.",
                "Add closing quote.",
                example, null, errorLength

        );
        return null;
    }

    private String readCharacter() {
        this.stringBuilder.setLength(0);
        int errorLength = 1;
        
        if (isAtEnd()) {
            String example = "\'x\'";
            
            advance();
            lexicalError(
                DiagnosticCode.E004,
                "Unclosed character literal.",
                "Add closing '.",
                example,  
                null, 
                errorLength);
        }

        char c = advance();
        String value;

        if (c == '\\') {
            if (isAtEnd()) {
                String example = "\'\\n\'";

                advance();
                lexicalError(
                    DiagnosticCode.E004,
                    "Unclosed character literal.",
                    "Add closing '.",
                    example, 
                    null, 
                    errorLength+2);
            }

            char escaped = advance();

            if (escaped == 'n') {
                value = "\\n";
            } else if (escaped == 't') {
                value = "\\t";
            } else if (escaped == '\'') {
                value = "'";
            } else if (escaped == '\\') {
                value = "\\\\";
            } else {
                String example = "'\\n'";

                lexicalError(
                    DiagnosticCode.E004,
                    "Invalid escape in character literal: \\" + escaped,
                    "Use valid escapes.",
                    example, 
                    null,  
                    errorLength);
                return null;
            }

        } else {
            value = String.valueOf(c);
        }

        if (isAtEnd()) {
            String example = "character charVariable = '" + value + "';";

            advance();
            lexicalError(
                DiagnosticCode.E004,
                "Unclosed character literal.",
                "Add closing '.",
                example,
                null, 
                1);
            return null;
        }

        if (peek() != '\'') {
            this.stringBuilder.append(value);

            while (!isAtEnd() && peek() != '\'') {
                this.stringBuilder.append(advance());
            }

            String allChars = this.stringBuilder.toString();
            errorLength = allChars.length();

            String example = "character charVariable = '" + value + "';\n" +
                            "// or\n" +
                            "  String stringVariable = \"" + allChars + "\";";

            lexicalError(
                DiagnosticCode.E103,
                "Character literal can only contain one character.",
                "Remove extra characters or use a string literal.",
                example,
                "Furthermore, character types can only have one character",
                errorLength);
        }

        advance();
        return value;
    }   

    private String readIdentifier() {
        this.stringBuilder.setLength(0);

        
        this.stringBuilder.append(advance());
        while (Character.isLetterOrDigit(peek()) || peek() == '_') {
            this.stringBuilder.append(advance());
        }

        return this.stringBuilder.toString();
    }

    private String readNumber() {
        this.stringBuilder.setLength(0);

        while (Character.isDigit(peek())) {
            this.stringBuilder.append(advance());
        }

        if (peek() == '.' && Character.isDigit(peekNext())) {
            this.stringBuilder.append(advance());

            while (Character.isDigit(peek())) {
                this.stringBuilder.append(advance());
            }
        }

        if (Character.isLetter(peek())) {
            String example = "integer numberVariable = " + this.stringBuilder.toString() + ";";

            this.stringBuilder.setLength(0);
            while (!isAtEnd() && Character.isLetter(peek())){
                this.stringBuilder.append(advance());
            }

            int errorLenth = this.stringBuilder.length();

            lexicalError(
                    DiagnosticCode.E101,
                    "Invalid numeric literal.",
                    "Numbers cannot be followed by letters.",
                    example, null, (errorLenth));
        }

        return this.stringBuilder.toString();
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
            String example,
            String note,
            int lenth) {

        throw new LexicalException(
                code,
                new SourceLocation(filePath, line, Math.max(column - 1, 0)),
                sourceManager.getContextLines(line, 2),
                cause,
                fix,
                example,
                note,
            lenth);
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
        tokensTypeByString.put("or", TokenType.OR);
        tokensTypeByString.put("and", TokenType.AND);
        tokensTypeByString.put("because", TokenType.BECAUSE);

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
