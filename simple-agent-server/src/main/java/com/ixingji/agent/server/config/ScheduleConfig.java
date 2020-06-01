package com.ixingji.agent.server.config;

import lombok.Data;

@Data
public class ScheduleConfig {

    private String instanceName;

    private Integer threadCount;

    private Integer intervalInSeconds;

}
