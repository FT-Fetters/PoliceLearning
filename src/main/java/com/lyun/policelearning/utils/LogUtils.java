package com.lyun.policelearning.utils;


import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {

    /**
     * 输出到日志
     * @param log 日志内容
     * @param type 日志类型
     */
    @SneakyThrows
    public static void log(String log, String type, boolean enableIp, HttpServletRequest request){
        String path = PathTools.getRunPath() + "/log/";
        File logDir = new File(path);
        if (!logDir.exists())logDir.mkdirs();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(date);
        File logFile = new File(path+time+".log");
        boolean writeable = logFile.setWritable(true, false);
        String logStr = timeFormat.format(date) + " [" + type + "] ";
        if (enableIp)logStr += IpUtils.getIpAddr(request) + " ";
        logStr+=log+"\n";
        System.out.print(logStr);
        if (logFile.exists()){
            FileWriter fileWriter = new FileWriter(logFile,true);
            fileWriter.append(logStr);
            fileWriter.close();
        }else {
            boolean newFile = logFile.createNewFile();
            if (newFile){
                FileWriter fileWriter = new FileWriter(logFile);
                fileWriter.append(logStr);
                fileWriter.close();
            }
        }


    }

}
