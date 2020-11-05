package com.bin.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * ���̶߳�ʱ����
 */
public class MyCronTrigger2 {

    private static Logger logger = LogManager.getLogger(MyCronTrigger2.class);

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        //ͨ��  SchedulerFactory ��ȡ����ʵ��
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        /**
         * ����JobDetail ʵ������jobʵ��
         * ָ��job�����ƣ�����������ƣ��Լ��󶨵�job��
         */
        JobDetail job1 = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("job1","jobGroup")
                .usingJobData("name","����1")
                .build();
        /**
         * ������ȴ�������
         */
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1","jobTriggerGroup")
                .usingJobData("shellPath","test1.sh")
                //ÿ��8�㵽23��ÿ����ִ��һ��
                .withSchedule(CronScheduleBuilder.cronSchedule("0/1 1-15 8-11 * * ?"))
                .build();
        /**
         * ����ҵ�ʹ�����ע�ᵽ����������
         */
        Date fistRuntime = scheduler.scheduleJob(job1,trigger);
        logger.info(job1.getKey()+"��ʼ�����ڣ�"+fistRuntime);


        JobDetail job2 = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("job2","jobGroup")
                .usingJobData("name","����2")
                .build();
        /**
         * ������ȴ�������
         */
        trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger2","jobTriggerGroup")
                .usingJobData("shellPath","test2.sh")
                //ÿ��8�㵽23��ÿ����ִ��һ��
                .withSchedule(CronScheduleBuilder.cronSchedule("0/1 25 * * * ?"))
                .build();
        fistRuntime = scheduler.scheduleJob(job2,trigger);
        logger.info(job2.getKey()+"��ʼ�����ڣ�"+fistRuntime);

        //��������
        scheduler.start();

    }
}
