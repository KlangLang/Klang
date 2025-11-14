package org.klang.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine;

import picocli.AutoComplete;

@Command(name = "gen-completion", description = "Gera script de autocompletar para bash/zsh")
public class GenerateCompletion implements Runnable {

    @Override
    public void run() {
        CommandLine cmd = new CommandLine(new KMain());
        String script = AutoComplete.bash("k", cmd);
        System.out.println(script);
    }
}
