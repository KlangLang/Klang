package org.klang.cli.error;

import org.klang.cli.error.diagnostic.KcDiagnosticCode;

abstract public class KcCliException extends RuntimeException {
    protected final KcDiagnosticCode code;
    protected final String command;
    protected final String fix;

    public KcCliException(
        KcDiagnosticCode code,
        String command,
        String fix
    ){
        this.code = code;
        this.command = command;
        this.fix = fix;
    }

    protected final String[] getCommands(){
        return new String[]{"lex", "parse", "help", "gen-completion"};
    }

    @Override
    public final String getMessage() {
        return format();
    }

    @Override
    public String toString() {
        return format();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this; 
    }

    public abstract String format();
}