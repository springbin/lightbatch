package com.bin.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzManager {

    private static Logger logger = LogManager.getLogger(QuartzManager.class);

    //调度实例
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    /**
     * 添加定时任务
     * @param jobName  任务名称
     * @param jobGroupName 任务组名
     * @param triggerName  触发器名称
     * @param triggerGroupName  触发器组名
     * @param jobClass  任务
     * @param cron  触发时间设置
     */
    public static void addJob(String jobName,String jobGroupName,
                       String triggerName,String triggerGroupName,Class jobClass,String cron){
            try{
                logger.info("添加任务开始！");
                Scheduler scheduler = schedulerFactory.getScheduler();
                //任务执行类 ，任务名称，任务组
                JobDetail jobDetail = JobBuilder.newJob(jobClass)
                        .withIdentity(jobName,jobGroupName)
                        .build();
                //触发器
                CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName,triggerGroupName) //设置触发器名  触发器组名
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
                //调度器中设置 jobDetail 和trigger
                scheduler.scheduleJob(jobDetail,trigger);
                //启动
                if(!scheduler.isShutdown()){
                    scheduler.start();
                }
                logger.info("添加任务成功！");
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e);
                throw new RuntimeException(e.getMessage());
            }
    }


    /**
     * 修改定时任务的触发时间
     * @param jobName  任务名称
     * @param jobGroupName  任务组名
     * @param triggerName  触发器名称
     * @param triggerGroupName  触发器组名
     * @param cron 触发时间设置
     */
    public static void modifyJobTime(String jobName,String jobGroupName,
                                     String triggerName,String triggerGroupName,String cron){
        try{
            logger.info("修改任务开始！");
            //获取调度器
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if(trigger == null){
                return ;
            }
            String oldCron = trigger.getCronExpression();
            if(!oldCron.equals(cron)){
                /**
                 * 方式1：重置触发器
                 */
                //触发器
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerName,triggerGroupName)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                        .build();
                //重置触发器
                scheduler.rescheduleJob(triggerKey,trigger);

                /**
                 * 方式2：先删除，再创建一个新的job
                 */
                //JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName,jobGroupName));
                 //获取Job任务
                //Class<? extends  Job> jobClass = jobDetail.getJobClass();
                 //移除job
                //removeJob(jobName,jobGroupName,triggerName,triggerGroupName);
                //添加任务
                //addJob(jobName,jobGroupName,triggerName,triggerGroupName,jobClass,cron);
            }
            logger.info("修改任务成功！");
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 移除任务
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     */
    public static void removeJob(String jobName,String jobGroupName,String triggerName,String triggerGroupName){
        try{
            logger.info("移除任务开始！");
            //获取调度器
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,triggerGroupName);
            scheduler.pauseTrigger(triggerKey);//停止调度器
            scheduler.unscheduleJob(triggerKey);//移除调度器
            scheduler.deleteJob(JobKey.jobKey(jobName,jobGroupName));//删除调度任务
            logger.info("移除任务成功！");
        }catch(Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 启动所有定时任务
     */
    public static void startJobs(){
        try{
            logger.info("启动任务!");
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 停止
     */
    public static void stopDownJobs(){
        try{
            logger.info("停止任务");
            Scheduler scheduler = schedulerFactory.getScheduler();
            if(!scheduler.isShutdown()){
                scheduler.shutdown();
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e);
            throw new RuntimeException(e.getMessage());
        }
    }

}
