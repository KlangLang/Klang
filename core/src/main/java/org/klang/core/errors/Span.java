package org.klang.core.errors;

/**
 * Representa um intervalo (start..end) no arquivo.
 * startLine/column são 1-based.
 */
public final class Span {
    public final String fileName;
    public final int startLine;
    public final int startColumn;
    public final int endLine;
    public final int endColumn;

    public Span(String fileName, int startLine, int startColumn, int endLine, int endColumn) {
        this.fileName = fileName;
        this.startLine = startLine;
        this.startColumn = startColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    public boolean isSingleLine() {
        return startLine == endLine;
    }

    @Override
    public String toString() {
        return fileName + " em linha: " + startLine + " coluna:" + startColumn;
    }
}
