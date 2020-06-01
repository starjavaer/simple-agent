package com.ixingji.agent.guarder.model;

import lombok.Data;

@Data
public class AgentFileContent {

    private String secretId;

    private int serverPort;

    private String serverProcess;

}
