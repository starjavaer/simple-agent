package com.ixingji.agent.server.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;

public class HiActionHandler implements ActionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HiActionHandler.class);

    @Override
    public String doHandle(String requestData) throws Exception {
        LOGGER.info("hi action");
        return ManagementFactory.getRuntimeMXBean().getName();
    }

}
