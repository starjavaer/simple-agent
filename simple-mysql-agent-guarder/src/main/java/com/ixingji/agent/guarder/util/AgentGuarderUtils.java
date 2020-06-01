package com.ixingji.agent.guarder.util;

import com.ixingji.agent.guarder.config.AgentGuarderConfig;
import com.ixingji.agent.guarder.constant.AgentGuarderConstants;
import com.ixingji.agent.guarder.exception.AuthCheckFailedException;
import com.ixingji.agent.guarder.model.AgentFileContent;
import com.ixingji.agent.server.util.AssertUtils;
import com.ixingji.agent.server.util.CmdUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.*;
import java.util.*;

public class AgentGuarderUtils {

    public static boolean isLocalUsing(int port) throws IOException {
        String command = "cmd.exe /c netstat -ano | findstr " + port;
        if (Objects.equals(OSUtils.os(), OSUtils.OS.LINUX)) {
            command = "netstat -ano | grep " + port;
        }
        CmdUtils.ExecStat execStat = CmdUtils.exec(command);
        return execStat.isOk() && !execStat.getResults().isEmpty();
    }

    public static String agentHome() {
        String agentHome = System.getenv(AgentGuarderConstants.AGENT_HOME);
        if (agentHome != null && agentHome.endsWith(File.pathSeparator)) {
            agentHome = agentHome.substring(0, agentHome.length() - 1);
            System.setProperty(AgentGuarderConstants.AGENT_HOME, agentHome);
        }
        return System.getenv(AgentGuarderConstants.AGENT_HOME);
    }

    public static String confDir() {
        String agentHome = agentHome();
        String configDir;
        if (agentHome == null) {
            agentHome = AgentGuarderUtils.class.getResource("/").getPath();
            configDir = agentHome;
        } else {
            configDir = agentHome + File.pathSeparator + AgentGuarderConstants.CONF_DIR_NAME;
        }
        return configDir;
    }

    public static Properties loadConfigProps() throws IOException {
        File confFile = new File(confDir(), AgentGuarderConstants.CONF_FILE_NAME);
        InputStream confInputStream = new FileInputStream(confFile);
        return PropertiesUtils.load(confInputStream);
    }

    public static void initConfig(AgentGuarderConfig guarderConfig) throws IOException {
        Properties properties = loadConfigProps();

        String guarderPort = properties.getProperty(AgentGuarderConfig.GUARDER_PORT);
        guarderConfig.setGuarderPort(guarderPort == null ? null : Integer.valueOf(guarderPort));

        String serverPortRange = properties.getProperty(AgentGuarderConfig.SERVER_PORT_RANGE);
        guarderConfig.setServerPortRange(serverPortRange);

        String targetBaseDir = properties.getProperty(AgentGuarderConfig.TARGET_BASE_DIR);
        guarderConfig.setTargetBaseDir(targetBaseDir);

        String targetNamePrefix = properties.getProperty(AgentGuarderConfig.TARGET_NAME_PREFIX);
        guarderConfig.setTargetNamePrefix(targetNamePrefix);

        Map<String, String> scripts = new HashMap<>();
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String propertyName = (String) enumeration.nextElement();

            if (!propertyName.toLowerCase().startsWith(AgentGuarderConfig.SCRIPT_CONF_PREFIX + ".")) {
                continue;
            }

            String actionName = propertyName.substring(AgentGuarderConfig.SCRIPT_CONF_PREFIX.length() + 1);
            scripts.put(actionName, properties.getProperty(propertyName));
        }
        guarderConfig.setScripts(scripts);
    }

    public static int randomAvaliablePort() throws IOException {
        AgentGuarderConfig agentGuarderConfig = AgentGuarderConfig.getInstance();
        int fromPort = agentGuarderConfig.getServerPortFrom();
        int toPort = agentGuarderConfig.getServerPortTo();
        int randomPort = fromPort + new Random().nextInt(toPort - fromPort + 1);
        if (!isLocalUsing(randomPort)) {
            return randomPort;
        }
        return randomAvaliablePort();
    }

    public static String getServerJarPath(String version) {
        String agentHome = agentHome();
        AssertUtils.notNull(agentHome, "agent home is null");
        String serverJarName = AgentGuarderConfig.getInstance().getServerJarName();
        String serverJarFullName = serverJarName + "-" + version + ".jar";
        // ${agent_home}/servers/simple-agent-server-1.0.jar
        return agentHome + File.pathSeparator + AgentGuarderConstants.SERVERS_DIR_NAME + File.pathSeparator + serverJarFullName;
    }

    public static void startupAgentServer(String serverJarPath, int port, String key, String sign) throws Exception {
        Map<String, String> scripts = AgentGuarderConfig.getInstance().getScripts();
        String scriptsJsonStr = scripts == null ? null : JSONUtils.toStr(scripts);

        String command = "cmd.exe /c start /min java -jar "
                + serverJarPath
                + " -p " + port
                + " -k " + key
                + " -s " + sign;
        if (StringUtils.isNotBlank(scriptsJsonStr)) {
            command += " -x " + '\"' + JSONUtils.escape(scriptsJsonStr) + '\"';
        }

        if (Objects.equals(OSUtils.os(), OSUtils.OS.LINUX)) {
            command = "nohup java -jar "
                    + serverJarPath
                    + " -p " + port
                    + " -k " + key
                    + " -s " + sign;
            if (StringUtils.isNotBlank(scriptsJsonStr)) {
                command += " -x " + '\"' + JSONUtils.escape(scriptsJsonStr) + '\"';
            }
            command += " &";
        }
        CmdUtils.execAsync(command);
        Thread.sleep(3000);
    }

    public static String getDbServerDir(int dbPort) {
        AgentGuarderConfig agentGuarderConfig = AgentGuarderConfig.getInstance();
        return agentGuarderConfig.getTargetBaseDir()
                + File.pathSeparator
                + agentGuarderConfig.getTargetNamePrefix()
                + dbPort;
    }

    public static boolean checkDbServer(int dbPort) throws IOException {
        return new File(getDbServerDir(dbPort)).exists()
                && isLocalUsing(dbPort);
    }

    public static void createAgentFile(int dbPort, AgentFileContent agentFileContent) throws IOException {
        String dbServerDir = getDbServerDir(dbPort);
        try (FileWriter fileWriter = new FileWriter(new File(dbServerDir, AgentGuarderConstants.AGENT_FILE_NAME))) {
            fileWriter.write(JSONUtils.toStr(agentFileContent));
            fileWriter.flush();
        }
    }

    public static boolean deleteAgentFile(int dbPort) {
        String dbServerDir = getDbServerDir(dbPort);
        File agentFile = new File(dbServerDir, AgentGuarderConstants.AGENT_FILE_NAME);
        return agentFile.exists() && agentFile.delete();
    }

    public static void select1(int port, String user, String password) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:"
                            + port
                            + "/information_schema",
                    user,
                    password
            );

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT 1");

            String result = null;
            while (resultSet.next()) {
                result = resultSet.getString("1");
            }
            resultSet.close();

            if (result == null) {
                throw new AuthCheckFailedException("can't select 1");
            }
        } catch (Exception e) {
            throw new AuthCheckFailedException(e);
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

}
