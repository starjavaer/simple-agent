package com.ixingji.agent.guarder;

import org.apache.commons.cli.*;

public class GuarderStarter {

    public static void main(String[] args) throws ParseException {

        CommandLineParser commandLineParser = new DefaultParser();

        Options options = new Options();
        options.addOption("h", "help", false, "print usage information");
        options.addOption("i", "secretId", true, "secret id");

        CommandLine commandLine = commandLineParser.parse(options, args);


    }

}
