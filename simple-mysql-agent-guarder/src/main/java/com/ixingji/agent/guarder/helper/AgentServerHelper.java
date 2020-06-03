package com.ixingji.agent.guarder.helper;

import com.alibaba.fastjson.JSON;
import com.ixingji.agent.client.ActorClient;
import com.ixingji.agent.grpc.ActionResponse;
import com.ixingji.agent.guarder.constant.AgentGuarderConstants;
import com.ixingji.agent.guarder.exception.RegisterFailedException;
import com.ixingji.agent.guarder.exception.ServerJarNotFoundException;
import com.ixingji.agent.guarder.model.AgentFile;
import com.ixingji.agent.guarder.model.RelateZKRequest;
import com.ixingji.agent.guarder.util.AESUtils;
import com.ixingji.agent.guarder.util.AgentGuarderUtils;
import com.ixingji.agent.guarder.util.JSONUtils;
import com.ixingji.agent.guarder.util.OSUtils;
import com.ixingji.agent.server.util.AgentServerUtils;
import com.ixingji.agent.server.util.AssertUtils;
import com.ixingji.agent.server.util.CmdUtils;
import jdk.internal.agent.Agent;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;

public class AgentServerHelper {

    public static void check(String zkUrl, String dbName) throws SQLException {
        // 根据zkUrl+dbName，从zk获取dbServer信息
        int dbPort = 0;
        String username = null;
        String password = null;

        AgentGuarderUtils.select1(dbPort, username, password);
    }

    public static void start(String zkUrl, String dbName) throws Exception {
        // 1. 根据zkUrl+dbName，请求zk获取dbServer信息
        String dbIp = "";
        int dbPort = 0;
        String username = null;
        String password = null;
        String version = "1.0";

        // 2. 检查db server是否存活
        // （1）卷目录是否存在
        // （2）db server端口是否正常服务
        AssertUtils.isTrue(AgentGuarderUtils.checkDbServer(dbPort), "Unexpected db server state, db port:" + dbPort);

        int availablePort = AgentGuarderUtils.randomAvailablePort();

        // 3. 生成鉴权信息
        String secretKey = AESUtils.generateKey();
        String secretId = AESUtils.encode(String.valueOf(System.currentTimeMillis()));

        String sign = AgentServerUtils.doSign(secretId, secretKey);

        String serverJarPath = AgentGuarderUtils.getServerJarPath(version);

        if (!new File(serverJarPath).exists()) {
            throw new ServerJarNotFoundException("server jar not exists, path: " + serverJarPath);
        }

        // 4. 启动server
        AgentGuarderUtils.startupAgentServer(serverJarPath, availablePort, secretKey, sign);

        ActorClient actorClient = new ActorClient(dbIp + ":" + availablePort);

        // 5. 验证agent server是否正常对外服务
        ActionResponse hiResponse = actorClient.doAction(secretId, "HI", "i'm your guarder");

        if (hiResponse.getCode() == 0) {
            // 6. agent server关联zk
            RelateZKRequest relateZKRequest = new RelateZKRequest();
            relateZKRequest.setZkUrl(zkUrl);
            relateZKRequest.setSecretId(secretId);
            relateZKRequest.setServerPort(availablePort);

            ActionResponse relateZKResponse = actorClient.doAction(secretId, "RELATEZK", JSONUtils.toStr(relateZKRequest));

            if (relateZKResponse.getCode() != 0) {
                throw new RegisterFailedException("agent server failed to relate zk");
            }

            // 7. 在db server卷目录下创建agent文件，用来保活：健康检查失败后用来重启agent server
            AgentFile agentFile = new AgentFile();
            agentFile.setZkUrl(zkUrl);
            agentFile.setSecretId(secretId);
            agentFile.setServerPort(availablePort);
            agentFile.setServerProcess(hiResponse.getData());

            AgentGuarderUtils.createAgentFile(availablePort, agentFile);
        } else {
            throw new RegisterFailedException("agent server failed to startup");
        }
    }

    public static void stop(String zkUrl, String dbName) throws Exception {
        // 1. 根据zkUrl+dbName，请求zk获取dbServer信息
        String dbIp = "";
        int dbPort = 0;
        String username = null;
        String password = null;
        String version = "1.0";

        // 2. 读取agent文件内容并解析pid
        String agentServerPid;
        File file = new File(AgentGuarderUtils.getDbServerDir(dbPort), AgentGuarderConstants.AGENT_FILE_NAME);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String agentInfo = reader.readLine();
            AgentFile agentFile = JSON.parseObject(agentInfo, AgentFile.class);
            String[] agentServerProcessInfo = StringUtils.split(agentFile.getServerProcess(), "@");
            agentServerPid = agentServerProcessInfo[0];
        }

        AssertUtils.notNull(agentServerPid, "no pid resolved");

        // 3. 删除agent文件
        AssertUtils.isTrue(AgentGuarderUtils.deleteAgentFile(dbPort), "failed to delete agent file, path: " + AgentGuarderUtils.getDbServerDir(dbPort));

        // 4. 删除zk agent node
        AgentGuarderUtils.deleteAgentZKNode(zkUrl, dbName);

        // 5. kill agent server进程
        String killCommand = "";
        if (OSUtils.os() == OSUtils.OS.WINDOWS) {
            killCommand = "taskkill /pid " + agentServerPid + " /f";
        } else if (OSUtils.os() == OSUtils.OS.LINUX) {
            killCommand = "kill -9 " + agentServerPid;
        }

        if (StringUtils.isNotBlank(killCommand)) {
            CmdUtils.ExecStat execStat = CmdUtils.exec(killCommand);
            AssertUtils.isTrue(execStat.isOk(), "failed to kill agent server");
        }
    }

}
