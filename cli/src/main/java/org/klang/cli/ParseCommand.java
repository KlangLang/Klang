package org.klang.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.klang.core.lexer.Lexer;
import org.klang.core.lexer.Token;
import org.klang.core.parser.TokenStream;
import org.klang.core.parser.ast.ProgramNode;

@Command(
    name = "parse",
    description = "Parse file.k"
)
public class ParseCommand implements Runnable {

    @Parameters(paramLabel = "FILE")
    private File file;

    @Override
    public void run() {
        Path path = file.toPath();

        if (!path.getFileName().toString().endsWith(".k")) {
            return;
        }

        try {
            String source = Files.readString(path);

            Lexer lexer = new Lexer(source, file.getPath());
            List<Token> tokens = lexer.tokenizeSourceCode();

            TokenStream parser = new TokenStream(tokens);
            ProgramNode program = parser.parseProgram();

            System.out.println("Parsed successfully.");

        } catch (RuntimeException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException("Internal compiler error", e);
        }
    }
}
