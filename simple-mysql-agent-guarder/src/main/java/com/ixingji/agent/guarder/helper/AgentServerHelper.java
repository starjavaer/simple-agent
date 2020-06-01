package com.ixingji.agent.guarder.helper;

import com.ixingji.agent.guarder.util.AgentGuarderUtils;
import com.ixingji.agent.server.util.AssertUtils;

import java.io.IOException;
import java.sql.SQLException;

public class AgentServerHelper {

    public static void check(String zkUrl, String dbName) throws SQLException {
        // 根据zkUrl+dbName，从zk获取dbServer信息
        int dbPort = 0;
        String username = null;
        String password = null;

        AgentGuarderUtils.select1(dbPort, username, password);
    }

    public static void start(String zkUrl, String dbName) throws IOException {
        // 根据zkUrl+dbName，从zk获取dbServer信息
        int dbPort = 0;
        String username = null;
        String password = null;

        // 检查db server是否存活
        // 1. 目录是否存在
        // 2. 端口是否可用
        AssertUtils.isTrue(AgentGuarderUtils.checkDbServer(dbPort), "Unexpected db server state, db port:" + dbPort);

        int randomPort = AgentGuarderUtils.randomAvaliablePort();


    }

    public static void stop(String zkUrl, String dbName) {

    }

}
