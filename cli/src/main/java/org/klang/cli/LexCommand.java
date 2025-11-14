package org.klang.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.klang.core.errors.DiagnosticException;
import org.klang.core.errors.ErrorReporter;
import org.klang.core.lexer.Lexer;

@Command(name = "lex", description = "Mostra os tokens do arquivo")
public class LexCommand implements Runnable {

    @Parameters(paramLabel = "FILE")
    private File file;

    @Override
    public void run() {
        try {
            Path path = file.toPath();
            String source = Files.readString(path);

            Lexer lexer = new Lexer(source, file.getPath());

            lexer.tokenize().forEach(System.out::println);

        } catch (DiagnosticException e) {
            // Erro léxico com spans, notas, multi-line, etc.
            ErrorReporter.report(e.diagnostic);
            System.exit(1);

        } catch (Exception e) {
            // erro inesperado → mostrar stacktrace (para debug)
            e.printStackTrace();
            System.exit(2);
        }
    }
}
