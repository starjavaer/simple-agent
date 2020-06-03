package com.ixingji.agent.guarder.model;

import lombok.Data;

@Data
public class AgentFile {

    private String zkUrl;

    private String secretId;

    private int serverPort;

    private String serverProcess;

}
