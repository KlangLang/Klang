package org.klang.cli;

import picocli.CommandLine.Command;

@Command(name = "--version", description = "Mostra a versão da Klang")
public class VersionCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Klang version 0.0.1-dev");
    }
}
