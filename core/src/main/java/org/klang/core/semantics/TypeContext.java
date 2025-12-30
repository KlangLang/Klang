package org.klang.core.semantics;

import java.util.HashMap;
import java.util.Map;

public class TypeContext {

    private final TypeContext parent;
    private final Map<String, TypeSymbol> symbols = new HashMap<>();

    public TypeContext(TypeContext parent) {
        this.parent = parent;
    }

    public void declare(String name, TypeSymbol type) {
        if (symbols.containsKey(name)) {
            throw new RuntimeException(
                "Variable '" + name + "' already declared in this scope"
            );
        }
        symbols.put(name, type);
    }

    public TypeSymbol resolve(String name) {
        if (symbols.containsKey(name)) {
            return symbols.get(name);
        }
        if (parent != null) {
            return parent.resolve(name);
        }
        throw new RuntimeException("Undefined variable '" + name + "'");
    }
}
