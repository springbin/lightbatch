package com.bin.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class MyCronTrigger {

    public static void main(String[] args) throws SchedulerException {

        //��������
        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("simpleJob","jobGroup")
                .usingJobData("name","��������2")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("cronTrigger","jobTriggerGroup")
                .usingJobData("shellPath","test.sh")
                //ÿ��8�㵽23��ÿ����ִ��һ��
                .withSchedule(CronScheduleBuilder.cronSchedule("0/1 * 8-23 * * ?"))
                .build();

        //ͨ��  SchedulerFactory ��ȡ����ʵ��
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.scheduleJob(jobDetail,trigger);
        //��������
        scheduler.start();
    }
}
