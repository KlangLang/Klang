package org.klang.core.semantics;

public final class ConstantSymbol implements TypeSymbol {
    public final TypeSymbol type;

    public ConstantSymbol(TypeSymbol type) {
        this.type = type;  // Fixed: was calling super() instead
    }

    @Override
    public boolean isAssignableFrom(TypeSymbol other) {
        return type.isAssignableFrom(other);
    }

    @Override
    public boolean isString() {
        return type.isString();  // Delegate to wrapped type
    }

    @Override
    public boolean isReference() {
        return type.isReference();
    }

    @Override
    public String toString() {
        return "constant " + type;
    }
}