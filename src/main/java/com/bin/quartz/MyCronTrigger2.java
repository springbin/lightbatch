package com.bin.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * 多线程定时任务
 */
public class MyCronTrigger2 {

    private static Logger logger = LogManager.getLogger(MyCronTrigger2.class);

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        //通过  SchedulerFactory 获取调度实例
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        /**
         * 创建JobDetail 实例，绑定job实例
         * 指定job的名称，所在组的名称，以及绑定的job类
         */
        JobDetail job1 = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("job1","jobGroup")
                .usingJobData("name","任务1")
                .build();
        /**
         * 定义调度触发规则
         */
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1","jobTriggerGroup")
                .usingJobData("shellPath","test1.sh")
                //每天8点到23点每分钟执行一次
                .withSchedule(CronScheduleBuilder.cronSchedule("0/1 1-15 8-11 * * ?"))
                .build();
        /**
         * 把作业和触发器注册到调度任务中
         */
        Date fistRuntime = scheduler.scheduleJob(job1,trigger);
        logger.info(job1.getKey()+"开始运行于："+fistRuntime);


        JobDetail job2 = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("job2","jobGroup")
                .usingJobData("name","任务2")
                .build();
        /**
         * 定义调度触发规则
         */
        trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger2","jobTriggerGroup")
                .usingJobData("shellPath","test2.sh")
                //每天8点到23点每分钟执行一次
                .withSchedule(CronScheduleBuilder.cronSchedule("0/1 25 * * * ?"))
                .build();
        fistRuntime = scheduler.scheduleJob(job2,trigger);
        logger.info(job2.getKey()+"开始运行于："+fistRuntime);

        //启动调度
        scheduler.start();

    }
}
