package com.bin.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuartzManagerTest {

    private static Logger logger = LogManager.getLogger(QuartzManagerTest.class);

    public static String JOB_NAME = "job";
    public static String JOB_GROUP_NAME="jobName";
    public static String TRIGGER_NAME="trigger";
    public static String TRIGGER_GROUP_NAME="triggerGroupName";

    public static void main(String[] args) throws InterruptedException {

        logger.info("【系统启动】");
        QuartzManager.addJob(JOB_NAME,JOB_GROUP_NAME,TRIGGER_NAME,TRIGGER_GROUP_NAME, SimpleJob.class,
                "0/1 * 8-23 * * ?");

        Thread.sleep(5000);
        logger.info("【修改时间】");
        QuartzManager.modifyJobTime(JOB_NAME,JOB_GROUP_NAME,TRIGGER_NAME,TRIGGER_GROUP_NAME,
                "0/5 * 8-23 * * ?");

        Thread.sleep(60000);
        logger.info("【移除定时】");
        QuartzManager.removeJob(JOB_NAME,JOB_GROUP_NAME,TRIGGER_NAME,TRIGGER_GROUP_NAME);

    }

}
