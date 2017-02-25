package com.self.deploy.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by shaojieyue
 * Created time 2017-02-21 20:51
 */
public class ShellUtil {
    private static final Logger logger = LoggerFactory.getLogger(ShellUtil.class);

    /**
     * 执行脚本
     * @param args
     * @param shell
     * @return String[] [0]:shell exit value [1]:shell log
     */
    public static String[] exec( String[] shell,Map<String, String> args){
        String[] arr = new String[2];
        StringBuilder buffer = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder(shell);

        Map<String, String> sysenv = pb.environment();
        if (args != null) {
            sysenv.putAll(args);
        }

        pb.redirectErrorStream(true);

        Process process = null;
        BufferedReader br = null;
        try {
            process = pb.start();
            InputStream in = process.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            String tmp;
            while ((tmp = br.readLine()) != null) {
                buffer.append(tmp + "\r\n");
                logger.info("==> "+tmp);
            }
            in.close();
        } catch (IOException e) {
            logger.error("Exceprion",e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                logger.error("Exceprion",e);
            }
            if(process != null){
                process.destroy();
            }
            try{
                try {
                    arr[0]=process.waitFor()+"";
                } catch (InterruptedException e) {
                    arr[0]="1";
                    logger.error("Exceprion",e);
                }
            }catch (IllegalThreadStateException e) {//shell 脚本缺少exit
                arr[0]="0";
                logger.error("Exceprion",e);
            }
            arr[1] = buffer.toString();
        }
        logger.info("shell "+shell+" exec value="+arr[0]);
        return arr;
    }

    /**
     * 执行脚本
     * @param args
     * @param shell
     * @return String[] [0]:shell exit value [1]:shell log
     */
    public static String[] exec( String shell,Map<String, String> args){
        return exec(new String[]{shell},args);
    }

}
