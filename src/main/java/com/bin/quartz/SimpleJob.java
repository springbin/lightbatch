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
            //获取创建的工作详情
            JobDetail detail = jobContext.getJobDetail();
            //获取工作的名称
            //String name = detail.getJobDataMap().getString("name");
            String job = detail.getKey().getGroup();
            String jobName = detail.getKey().getName();
            //获取数据
            JobDataMap jobDataMap = jobContext.getTrigger().getJobDataMap();
            String shellPath=jobDataMap.getString("shellPath");
            logger.info("线程调度组："+job+"，任务名："+jobName+"----->"+shellPath);
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
