package org.klang.cli;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.klang.core.errors.Diagnostic;
import org.klang.core.errors.DiagnosticException;
import org.klang.core.errors.DiagnosticPrinter;
import org.klang.core.errors.DiagnosticType;
import org.klang.core.errors.Note;
import org.klang.core.errors.Span;
import org.klang.core.lexer.Lexer;
import org.klang.core.lexer.Token;
import org.klang.core.parser.TokenStream;
import org.klang.core.parser.ast.ProgramNode;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "parse", description = "Parse file.k")
public class ParseCommand implements Runnable {
    @Parameters(paramLabel = "FILE")
    private File file;

    @Override
    public void run() {
        try {
            Path path = file.toPath();

            if (!path.getFileName().toString().endsWith(".k")) {

                Span span = new Span(null, 1, 1, 1, 1); // fileless → works in any context

                Diagnostic d = Diagnostic.builder(DiagnosticType.TYPE, "The file must be a .k file")
                        .primary(span)
                        .addNote(new Note("Maybe you got confused with another file type?"))
                        .build();

                throw new DiagnosticException(d);
            }

            String source = Files.readString(path);

            Lexer lexer = new Lexer(source, file.getPath());
            List<Token> tokens = lexer.tokenizeSourceCode();

            TokenStream parser = new TokenStream(tokens);
            ProgramNode program = parser.parseProgram();

            System.out.println("Parsed successfully.");


        } catch (DiagnosticException e) {
            DiagnosticPrinter printer = new DiagnosticPrinter(true, true);
            printer.print(e.diagnostic);

            System.exit(1);
        } catch (Exception e) {
            // unexpected errors → stacktrace
            e.printStackTrace();
            System.exit(2);
        }
    }    
}
