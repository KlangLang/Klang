package org.klang.core.semantics;

public final class PrimitiveTypeSymbol implements TypeSymbol {
    public final Type type;

    public PrimitiveTypeSymbol(Type type) {
        this.type = type;
    }

    @Override
    public boolean isAssignableFrom(TypeSymbol other) {
        if (other instanceof PrimitiveTypeSymbol p) {
            return p.type == this.type;
        }

        if (other instanceof PrimitiveTypeSymbol p
            && p.type == Type.NULL
            && type == Type.STRING) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isReference() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isString() {
        return type == Type.STRING;
    }


    @Override
    public String toString() {
        return type.name().toLowerCase();
    }
}
