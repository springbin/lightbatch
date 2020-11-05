package com.bin.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzManager {

    private static Logger logger = LogManager.getLogger(QuartzManager.class);

    //����ʵ��
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    /**
     * ��Ӷ�ʱ����
     * @param jobName  ��������
     * @param jobGroupName ��������
     * @param triggerName  ����������
     * @param triggerGroupName  ����������
     * @param jobClass  ����
     * @param cron  ����ʱ������
     */
    public static void addJob(String jobName,String jobGroupName,
                       String triggerName,String triggerGroupName,Class jobClass,String cron){
            try{
                logger.info("�������ʼ��");
                Scheduler scheduler = schedulerFactory.getScheduler();
                //����ִ���� ���������ƣ�������
                JobDetail jobDetail = JobBuilder.newJob(jobClass)
                        .withIdentity(jobName,jobGroupName)
                        .build();
                //������
                CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName,triggerGroupName) //���ô�������  ����������
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
                //������������ jobDetail ��trigger
                scheduler.scheduleJob(jobDetail,trigger);
                //����
                if(!scheduler.isShutdown()){
                    scheduler.start();
                }
                logger.info("�������ɹ���");
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e);
                throw new RuntimeException(e.getMessage());
            }
    }


    /**
     * �޸Ķ�ʱ����Ĵ���ʱ��
     * @param jobName  ��������
     * @param jobGroupName  ��������
     * @param triggerName  ����������
     * @param triggerGroupName  ����������
     * @param cron ����ʱ������
     */
    public static void modifyJobTime(String jobName,String jobGroupName,
                                     String triggerName,String triggerGroupName,String cron){
        try{
            logger.info("�޸�����ʼ��");
            //��ȡ������
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if(trigger == null){
                return ;
            }
            String oldCron = trigger.getCronExpression();
            if(!oldCron.equals(cron)){
                /**
                 * ��ʽ1�����ô�����
                 */
                //������
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerName,triggerGroupName)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                        .build();
                //���ô�����
                scheduler.rescheduleJob(triggerKey,trigger);

                /**
                 * ��ʽ2����ɾ�����ٴ���һ���µ�job
                 */
                //JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName,jobGroupName));
                 //��ȡJob����
                //Class<? extends  Job> jobClass = jobDetail.getJobClass();
                 //�Ƴ�job
                //removeJob(jobName,jobGroupName,triggerName,triggerGroupName);
                //�������
                //addJob(jobName,jobGroupName,triggerName,triggerGroupName,jobClass,cron);
            }
            logger.info("�޸�����ɹ���");
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * �Ƴ�����
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     */
    public static void removeJob(String jobName,String jobGroupName,String triggerName,String triggerGroupName){
        try{
            logger.info("�Ƴ�����ʼ��");
            //��ȡ������
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,triggerGroupName);
            scheduler.pauseTrigger(triggerKey);//ֹͣ������
            scheduler.unscheduleJob(triggerKey);//�Ƴ�������
            scheduler.deleteJob(JobKey.jobKey(jobName,jobGroupName));//ɾ����������
            logger.info("�Ƴ�����ɹ���");
        }catch(Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * �������ж�ʱ����
     */
    public static void startJobs(){
        try{
            logger.info("��������!");
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * ֹͣ
     */
    public static void stopDownJobs(){
        try{
            logger.info("ֹͣ����");
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
