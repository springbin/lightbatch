package com.bin.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class MySimpleTrigger {

    private static Logger logger = LogManager.getLogger(MySimpleTrigger.class);

    public static void main(String[] args) throws SchedulerException, InterruptedException {
       //��������
        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("simpleJob","jobGroup")
                .usingJobData("name","��������")
                .build();

        //����һ������������ÿ5�봥��һ���ܼ�3��
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("simpleTrigger","jobTriggerGroup")
                .startNow()// StartNow Ӧ��ֻ������ SimpleTrigger ������
                .usingJobData("shellPath","test.sh")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5).withRepeatCount(3))// 3ִ��4��   0ִ��1��
                .build();

        //ͨ��  SchedulerFactory ��ȡ����ʵ��
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.scheduleJob(jobDetail,trigger);
        //��������
        scheduler.start();

        //�ȴ�60��ʹ���ǵ�job�л���ִ��
        Thread.sleep(60000);

        //�ȴ���ҵִ�����ʱ�Źرյ�����
        scheduler.shutdown(true);

        SchedulerMetaData metaData = scheduler.getMetaData();

        logger.info("һ�������ˣ�"+metaData.getNumberOfJobsExecuted()+"������");

    }
}
