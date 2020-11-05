package com.bin.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class MySimpleTrigger {

    private static Logger logger = LogManager.getLogger(MySimpleTrigger.class);

    public static void main(String[] args) throws SchedulerException, InterruptedException {
       //定义任务
        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("simpleJob","jobGroup")
                .usingJobData("name","批量任务")
                .build();

        //定义一个触发器规则每5秒触发一次总计3次
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("simpleTrigger","jobTriggerGroup")
                .startNow()// StartNow 应该只适用于 SimpleTrigger 触发器
                .usingJobData("shellPath","test.sh")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5).withRepeatCount(3))// 3执行4次   0执行1次
                .build();

        //通过  SchedulerFactory 获取调度实例
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.scheduleJob(jobDetail,trigger);
        //启动调度
        scheduler.start();

        //等待60秒使我们的job有机会执行
        Thread.sleep(60000);

        //等待作业执行完成时才关闭调度器
        scheduler.shutdown(true);

        SchedulerMetaData metaData = scheduler.getMetaData();

        logger.info("一共运行了："+metaData.getNumberOfJobsExecuted()+"个任务");

    }
}
