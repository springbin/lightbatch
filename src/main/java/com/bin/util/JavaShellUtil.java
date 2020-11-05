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
            logger.info("׼��ִ��Shell���� ");
            Process pid = null;
            String[] cmd = { "/bin/sh", "-c", shellCommand };
            //ִ��Shell����
            pid = Runtime.getRuntime().exec(cmd);
            if (pid != null) {
                logger.info("���̺ţ�"+pid.toString());
                bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);
                pid.waitFor();
            } else {
                logger.info("û��pid");
            }
            logger.info("Shell����ִ�����");
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            //��ȡShell��������ݣ�����ӵ�stringBuffer��
            while (bufferedReader != null && (line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line).append("\r\n");
            }
            logger.info("Shell����ִ�н��Ϊ��"+stringBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ִ��Shell����ʱ�����쳣��"+e.getMessage());
            success = 1;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return success;
    }

}
