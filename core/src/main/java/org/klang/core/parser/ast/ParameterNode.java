package org.klang.core.parser.ast;

import org.klang.core.lexer.Token;

public class ParameterNode {
    public final Token type;
    public final Token name;

    public ParameterNode(Token type, Token name){
        this.type = type;
        this.name = name;
    }
}
