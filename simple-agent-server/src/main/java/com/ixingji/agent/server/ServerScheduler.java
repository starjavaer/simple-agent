package com.ixingji.agent.server;

import com.ixingji.agent.server.config.AgentServerConfig;
import com.ixingji.agent.server.config.ScheduleConfig;
import com.ixingji.agent.server.exception.JobNotFoundException;
import com.ixingji.agent.server.util.AgentServerUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Properties;

public class ServerScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerScheduler.class);

    public static void schedule(String jobName) throws SchedulerException, JobNotFoundException {
        AgentServerConfig agentServerConfig = AgentServerConfig.getInstance();
        ScheduleConfig scheduleConfig = agentServerConfig.getScheduleConfig();

        Properties quartzProps = AgentServerUtils.quartzProps(scheduleConfig);

        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory(quartzProps);
        Scheduler scheduler = schedulerFactory.getScheduler();

        String defaultName = scheduleConfig.getInstanceName().toUpperCase();

        Date startTime = DateBuilder.nextGivenSecondDate(null, 10);

        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder
                                .simpleSchedule()
                                .withIntervalInSeconds(scheduleConfig.getIntervalInSeconds())
                                .repeatForever()
                                .withMisfireHandlingInstructionNextWithRemainingCount()
                ).build();

        String jobClazzName = agentServerConfig.getJobs().get(jobName);
        if (jobClazzName == null || "".equals(jobClazzName)) {
            throw new JobNotFoundException("Job '" + jobName + "' doesn't exist");
        }

        Class<Job> jobClazz;
        try {
            jobClazz = (Class<Job>) Class.forName(jobClazzName);
        } catch (ClassNotFoundException e) {
            LOGGER.error("job class not found", e);
            throw new JobNotFoundException("Job class '" + jobClazzName + "' doesn't exist");
        }

        JobDetail jobDetail = JobBuilder.newJob(jobClazz)
                .withIdentity(jobName, defaultName)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);

        scheduler.start();
    }

}
