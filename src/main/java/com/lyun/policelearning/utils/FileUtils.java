package com.lyun.policelearning.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class FileUtils {

    public static String txt2String(File file) throws IOException {
        StringBuilder result = new StringBuilder();
        if (file.setReadable(true)) {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()).append(s);
            }
            br.close();
        }
        return result.toString();
    }

    public static byte[] getResourceFile(String path, ClassLoader classLoader) throws IOException {
        if (path == null || path.equals(""))return null;
        ClassPathResource classPathResource = new ClassPathResource(path);
        File file = classPathResource.getFile();
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] res = new byte[fileInputStream.available()];
        int read = fileInputStream.read(res);
        fileInputStream.close();
        if (read == res.length)
            return res;
        else return null;



    }
}
