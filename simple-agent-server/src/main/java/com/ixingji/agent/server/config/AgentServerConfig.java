package com.ixingji.agent.server.config;

import com.ixingji.agent.server.ServerActor;
import lombok.Data;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

@Data
public class AgentServerConfig {

    private static final String CONFIG_FILE = "/agent.yml";

    private static AgentServerConfig instance = loadConfig();

    private String secretKey;

    private String sign;

    private Map<String, String> commandActions;

    private ScheduleConfig scheduleConfig;

    private Map<String, String> jobs;

    private Map<String, String> actions;

    private static AgentServerConfig loadConfig() {
        Yaml yaml = new Yaml();
        return yaml.loadAs(ServerActor.class.getResourceAsStream(CONFIG_FILE),
                AgentServerConfig.class);
    }

    public static AgentServerConfig getInstance() {
        return instance;
    }

    private AgentServerConfig() {

    }

}
