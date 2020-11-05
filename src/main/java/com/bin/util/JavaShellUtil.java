package com.bin.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JavaShellUtil {

    private static Logger logger = LogManager.getLogger(JavaShellUtil.class);

    public int executeShell(String shellCommand) throws IOException {
        int success = 0;
        BufferedReader bufferedReader = null;
        try {
            logger.info("准备执行Shell命令 ");
            Process pid = null;
            String[] cmd = { "/bin/sh", "-c", shellCommand };
            //执行Shell命令
            pid = Runtime.getRuntime().exec(cmd);
            if (pid != null) {
                logger.info("进程号："+pid.toString());
                bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);
                pid.waitFor();
            } else {
                logger.info("没有pid");
            }
            logger.info("Shell命令执行完毕");
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            //读取Shell的输出内容，并添加到stringBuffer中
            while (bufferedReader != null && (line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line).append("\r\n");
            }
            logger.info("Shell命令执行结果为："+stringBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("执行Shell命令时发生异常："+e.getMessage());
            success = 1;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return success;
    }

}
