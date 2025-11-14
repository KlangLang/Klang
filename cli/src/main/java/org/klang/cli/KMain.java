package org.klang.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine;

@Command(name = "k", subcommands = {
        LexCommand.class,
        VersionCommand.class,
        GenerateCompletion.class
})
public class KMain implements Runnable {

    @Override
    public void run() {
        System.out.println("Klang CLI - Use 'k --help'");
    }

    public static void main(String[] args) {
        int exit = new CommandLine(new KMain()).execute(args);
        System.exit(exit);
    }
}
