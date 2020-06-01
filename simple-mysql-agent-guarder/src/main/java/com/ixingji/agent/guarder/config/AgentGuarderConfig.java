package com.ixingji.agent.guarder.config;

import com.ixingji.agent.guarder.util.AgentGuarderUtils;
import com.ixingji.agent.server.config.AgentServerConfig;
import com.ixingji.agent.server.util.AssertUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class AgentGuarderConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentGuarderConfig.class);

    private static AgentGuarderConfig instance = new AgentGuarderConfig();

    static {
        try {
            AgentGuarderUtils.initConfig(instance);
        } catch (IOException e) {
            LOGGER.error("init config error", e);
        }
    }

    public static final String GUARDER_PORT = "guarderPort";

    public static final String SERVER_PORT_RANGE = "serverPortRange";

    public static final String SERVER_PORT = "serverPort";

    public static final String TARGET_BASE_DIR = "targetBaseDir";

    public static final String TARGET_NAME_PREFIX = "targetNamePrefix";

    public static final String SCRIPT_CONF_PREFIX = "script";

    /**
     * guarder端口
     */
    @Getter
    @Setter
    private Integer guarderPort;

    /**
     * server jar名称
     */
    @Getter
    @Setter
    private String serverJarName = "simple-agent-server";

    /**
     * server使用端口范围（闭区间）
     */
    private String serverPortRange;

    /**
     * 起始端口号
     */
    @Getter
    private Integer serverPortFrom;

    /**
     * 截止端口号
     */
    @Getter
    private Integer serverPortTo;

    /**
     * 目标目录
     */
    @Getter
    @Setter
    private String targetBaseDir;

    /**
     * 目标名称前缀
     */
    @Getter
    @Setter
    private String targetNamePrefix;

    /**
     * 脚本
     */
    @Getter
    @Setter
    private Map<String, String> scripts;


    public void setServerPortRange(String serverPortRange) {
        this.serverPortRange = serverPortRange;
        String[] rangePorts = StringUtils.split(serverPortRange, "-");
        AssertUtils.isTrue(rangePorts.length == 2, "serverPortRange:" + serverPortRange + " is illegal");
        this.serverPortFrom = Integer.valueOf(rangePorts[0]);
        this.serverPortTo = Integer.valueOf(rangePorts[1]);
    }

    public static AgentGuarderConfig getInstance() {
        return instance;
    }

    private AgentGuarderConfig() {

    }

}
