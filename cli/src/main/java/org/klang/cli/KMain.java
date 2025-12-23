package org.klang.cli;

import org.klang.core.error.KException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "kc",
    description = "Klang CLI",
    mixinStandardHelpOptions = false,
    versionProvider = KVersionProvider.class,
    subcommands = {
        LexCommand.class,
        GenerateCompletion.class,
        HelpCommand.class,
        ParseCommand.class
    }
)
public class KMain implements Runnable {

    @Option(
        names = { "-h", "--help" },
        description = "Show this help catalog"
    )
    boolean help;

    @Option(
        names = { "-V", "--version" },
        versionHelp = true,
        description = "Show Klang version"
    )
    boolean version;

    @Override
    public void run() {
        if (help) {
            new HelpCommand().run();
            return;
        }

        if (version) {
            System.out.println(new KVersionProvider().getVersion()[0]);
            return;
        }

        new HelpCommand().run();
    }

    public static void main(String[] args) {
        try {
            int exitCode = new CommandLine(new KMain()).execute(args);
            System.exit(exitCode);
            
        } catch (KException e) {
            System.out.println("Saindo por aqui");
            System.out.println(e.format());
        }
    }
}
