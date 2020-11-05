package com.bin.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class MyCronTrigger {

    public static void main(String[] args) throws SchedulerException {

        //定义任务
        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("simpleJob","jobGroup")
                .usingJobData("name","批量任务2")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("cronTrigger","jobTriggerGroup")
                .usingJobData("shellPath","test.sh")
                //每天8点到23点每分钟执行一次
                .withSchedule(CronScheduleBuilder.cronSchedule("0/1 * 8-23 * * ?"))
                .build();

        //通过  SchedulerFactory 获取调度实例
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.scheduleJob(jobDetail,trigger);
        //启动调度
        scheduler.start();
    }
}
