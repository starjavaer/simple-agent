package com.ixingji.agent.server;

import com.google.gson.internal.$Gson$Preconditions;
import com.ixingji.agent.server.config.AgentServerConfig;
import com.ixingji.agent.server.util.AgentServerUtils;
import com.ixingji.agent.server.util.AssertUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerStarter.class);

    public static void main(String[] args) throws Exception {
        // 解析参数
        CommandLine commandLine = AgentServerUtils.parseCommandLine(args);

        String serverPort = commandLine.getOptionValue('p');
        String secretKey = commandLine.getOptionValue('k');
        String sign = commandLine.getOptionValue('s');
        String scripts = commandLine.getOptionValue('x');

        if (serverPort == null) {
            serverPort = "8443";
        }

        AssertUtils.notNull(secretKey, "key is null");
        AssertUtils.notNull(sign, "sign is null");

        // 加载配置的action
        AgentServerUtils.initActionHandlers(
                AgentServerConfig.getInstance().getActions(),
                scripts
        );

        // 启动server
        ServerActor serverActor = new ServerActor();
        serverActor.start(Integer.parseInt(serverPort));

        serverActor.blockUntilShutdown();
    }

}
