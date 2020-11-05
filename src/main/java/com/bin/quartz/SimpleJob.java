package com.bin.quartz;

import com.bin.util.JavaShellUtil;
import com.bin.util.NetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

public class SimpleJob implements Job {

    private static Logger logger = LogManager.getLogger(SimpleJob.class);

    @Override
    public void execute(JobExecutionContext jobContext) throws JobExecutionException {
        try{
            //��ȡ�����Ĺ�������
            JobDetail detail = jobContext.getJobDetail();
            //��ȡ����������
            //String name = detail.getJobDataMap().getString("name");
            String job = detail.getKey().getGroup();
            String jobName = detail.getKey().getName();
            //��ȡ����
            JobDataMap jobDataMap = jobContext.getTrigger().getJobDataMap();
            String shellPath=jobDataMap.getString("shellPath");
            logger.info("�̵߳����飺"+job+"����������"+jobName+"----->"+shellPath);
            //JavaShellUtil shell = new JavaShellUtil();
            //int success = shell.executeShell("sh "+shellPath);
           // logger.info("shell result = "+success);
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e);
        }
//        String[] allLocalHostIPAddress = NetUtil.getAlllocalHostIPAddress();
//        for(String address : allLocalHostIPAddress){
//            logger.info(address);
//        }
    }
}
