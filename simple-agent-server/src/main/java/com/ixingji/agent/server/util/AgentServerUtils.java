package com.ixingji.agent.server.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.ixingji.agent.grpc.ActionRequest;
import com.ixingji.agent.grpc.ActionResponse;
import com.ixingji.agent.server.action.ActionHandler;
import com.ixingji.agent.server.action.ActionManager;
import com.ixingji.agent.server.action.CommandActionHandler;
import com.ixingji.agent.server.action.HiActionHandler;
import com.ixingji.agent.server.config.AgentServerConfig;
import com.ixingji.agent.server.config.ScheduleConfig;
import com.ixingji.agent.server.exception.SecretMismatchException;
import org.apache.commons.cli.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class AgentServerUtils {

    public static CommandLine parseCommandLine(String[] args) throws ParseException {
        CommandLineParser commandLineParser = new DefaultParser();

        Options options = new Options();
        options.addOption("h", "help", false, "print usage information");
        options.addOption("p", "port", true, "server port");
        options.addOption("k", "key", true, "secret key");
        options.addOption("s", "sign", true, "sign");
        options.addOption("x", "scripts", true, "scripts");

        return commandLineParser.parse(options, args);
    }

    public static Properties quartzProps(ScheduleConfig scheduleConfig) {
        Properties quartzProps = new Properties();
        quartzProps.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        quartzProps.setProperty("org.quartz.scheduler.instanceName", scheduleConfig.getInstanceName());
        quartzProps.setProperty("org.quartz.threadPool.threadCount", String.valueOf(scheduleConfig.getThreadCount()));
        return quartzProps;
    }

    public static void initActionHandlers(Map<String, String> actions, String scripts) throws Exception {
        ActionManager.registerHandler("HI", new HiActionHandler());

        if (actions != null) {
            for (Map.Entry<String, String> actionEntry : actions.entrySet()) {
                String actionName = actionEntry.getKey();
                String actionClazz = actionEntry.getValue();
                ActionManager.registerHandler(actionName.toUpperCase(), (ActionHandler) Class.forName(actionClazz).newInstance());
            }
        }

        if (scripts != null && !"".equals(scripts)) {
            return;
        }

        JSONObject actionCommands = JSON.parseObject(scripts);
        if (actionCommands == null || actionCommands.size() == 0) {
            return;
        }

        for (Map.Entry<String, Object> actionCommand : actionCommands.entrySet()) {
            ActionManager.registerHandler(actionCommand.getKey().toUpperCase(), new CommandActionHandler(String.valueOf(actionCommand.getValue())));
        }
    }

    public static String doSign(String secretId) {
        return DigestUtils.md5Hex(secretId + "/" + AgentServerConfig.getInstance().getSecretKey());
    }

    public static String doSign(String secretId, String secretKey) {
        return DigestUtils.md5Hex(secretId + "/" + secretKey);
    }

    public static void checkRequest(ActionRequest request) throws SecretMismatchException {
        String secretId = request.getSecretId();
        String sign = doSign(secretId);
        if (!Objects.equals(AgentServerConfig.getInstance().getSign(), sign)) {
            throw new SecretMismatchException("secret id:" + secretId + " mismatch");
        }
    }

}
