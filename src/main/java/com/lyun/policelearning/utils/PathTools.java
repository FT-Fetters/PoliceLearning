package com.lyun.policelearning.utils;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;

public class PathTools {
    //linux和windows下通用
    private static String getJarFilePath() {
        ApplicationHome home = new ApplicationHome(PathTools.class);
        File jarFile = home.getSource();
        return jarFile.getParentFile().toString();
    }

    /**
     * 获取运行环境所在的目录
     * @return 运行目录
     */
    public static String getRunPath(){
        File jarFile = new File(getJarFilePath());
        return jarFile.getParent() + "/policelearing";
    }
    public static String getImagePath(){
        File jarFile = new File(getJarFilePath());
        return jarFile.getParent()+"\\src\\main\\resources\\image";
    }
}
