package org.klang.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.klang.core.error.KException;
import org.klang.core.lexer.Lexer;

@Command(
    name = "lex",
    description = "Show file tokens"
)
public class LexCommand implements Runnable {

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
            lexer.tokenizeSourceCode().forEach(System.out::println);

        } catch (KException e) {
            System.out.println(e.format());

        } catch (Exception e) {
            throw new RuntimeException("Internal compiler error", e);
        }
    }
}
