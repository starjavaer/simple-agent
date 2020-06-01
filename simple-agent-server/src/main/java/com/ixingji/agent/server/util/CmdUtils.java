package com.ixingji.agent.server.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CmdUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmdUtils.class);

    @Data
    @AllArgsConstructor
    public static class ExecStat {

        private boolean ok;

        private List<String> results;

    }

    public static ExecStat exec(String command) throws IOException {
        List<String> lines = new ArrayList<>();

        Scanner inScanner = null;
        Scanner errScanner = null;

        Process process = null;

        try {
            process = Runtime.getRuntime().exec(command);

            InputStream inStream = process.getInputStream();
            inScanner = new Scanner(inStream);

            while (inScanner.hasNextLine()) {
                lines.add(inScanner.nextLine());
            }

            if (lines.size() > 0) {
                return new ExecStat(true, lines);
            }

            InputStream errStream = process.getErrorStream();
            errScanner = new Scanner(errStream);

            while (errScanner.hasNextLine()) {
                lines.add(errScanner.nextLine());
            }

            return new ExecStat(false, lines);
        } finally {
            if (inScanner != null) {
                inScanner.close();
            }

            if (errScanner != null) {
                errScanner.close();
            }

            if (process != null) {
                process.destroy();
            }
        }
    }

    public static int exec2(String command) throws IOException {
        CommandLine commandLine = CommandLine.parse(command);

        DefaultExecutor executor = new DefaultExecutor();
//        ExecuteStreamHandler streamHandler = new PumpStreamHandler();
//        executor.setStreamHandler(streamHandler);

        return executor.execute(commandLine);
    }

    public static void execAsync(final String command) throws IOException {
        final CommandLine commandLine = CommandLine.parse(command);

        Executor executor = new DaemonExecutor();
//        ExecuteWatchdog watchdog = new ExecuteWatchdog(10 * 1000);
//        executor.setWatchdog(watchdog);

        executor.execute(commandLine, new ExecuteResultHandler() {
            @Override
            public void onProcessComplete(int i) {
                LOGGER.info("command:{} Complete", commandLine);
            }

            @Override
            public void onProcessFailed(ExecuteException e) {
                LOGGER.error("command:{} Failed", commandLine);
            }
        });
    }

}
