package org.klang.core.parser.ast;

import org.klang.core.lexer.Token;

public class ConstantDeclarationNode extends StatementNode {
    public final Token name;
    public final TypeReferenceNode type;
    public final ExpressionNode value;

    public ConstantDeclarationNode(
        Token name,
        TypeReferenceNode type,
        ExpressionNode value,
        int line,
        int column
    ) {
        super(line, column);
        this.name = name;
        this.type = type;
        this.value = value;
    }
}
