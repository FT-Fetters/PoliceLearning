package com.lyun.policelearning.utils.page;

public class StringFileter {
    public static String filterSring(String str){
        str = str.replace("\n","<br>");
        str = str.replace("\r","<br>");
        if(str.contains("<br><br>")){
            str = str.replace("<br><br>","<br><br>&nbsp&nbsp&nbsp&nbsp&nbsp");
            return "&nbsp&nbsp&nbsp&nbsp&nbsp" + str;
        }else {
            str = str.replace("<br>","<br>&nbsp&nbsp&nbsp&nbsp&nbsp");
            return "&nbsp&nbsp&nbsp&nbsp&nbsp" + str;
        }
    }
}
