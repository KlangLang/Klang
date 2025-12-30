package org.klang.core.parser.ast;

import org.klang.core.lexer.Token;

public class ParameterNode {
    public final TypeReferenceNode type;
    public final Token name;

    public ParameterNode(TypeReferenceNode type, Token name){
        this.type = type;
        this.name = name;
    }
}
