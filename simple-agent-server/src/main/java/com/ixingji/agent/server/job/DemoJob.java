package com.ixingji.agent.server.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("Hello DemoJob");
    }

}
