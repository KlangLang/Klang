package org.klang.core.error;

public record SourceLocation(
    String file,
    int line,
    int column
) {}
