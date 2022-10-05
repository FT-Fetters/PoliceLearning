package com.lyun.policelearning.utils;

import com.lyun.policelearning.entity.question.Judgment;
import com.lyun.policelearning.entity.question.MultipleChoice;
import com.lyun.policelearning.entity.question.SingleChoice;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IDocUtil implements DocUtil {


    private static class SingletonHolder {
        private static final IDocUtil INSTANCE = new IDocUtil();
    }

    private IDocUtil() {
    }

    public static IDocUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private String curFile;

    private List<Judgment> curJud;

    private List<SingleChoice> curSingle;

    private List<MultipleChoice> curMultiple;

    private final static int JUDGMENT = 1;

    private final static int SINGLE = 2;

    private final static int MULTIPLE = 3;

    int state = -1;


    /**
     * 解析docx文件里面的数据
     */
    private void analyticalData(String filePath) {
        if (filePath.equals(curFile))
            return;
        else
            curFile = filePath;
        List<String> lines = readDocxFile(curFile);
        if (lines == null || lines.isEmpty()) return;
        curJud = new ArrayList<>();
        curSingle = new ArrayList<>();
        curMultiple = new ArrayList<>();
        Iterator<String> lineIter = lines.iterator();
        while (lineIter.hasNext()){
            analyticalLine(lineIter);
        }
    }


    private void analyticalLine(Iterator<String> lineIter) {
        String line = lineIter.next();
        if (line.startsWith("、单选题", 1)) {
            state = SINGLE;
            return;
        }
        if (line.startsWith("、多选题", 1)) {
            state = MULTIPLE;
            return;
        }
        if (line.startsWith("、判断题", 1)) {
            state = JUDGMENT;
            return;
        }
        if (state == -1)
            return;
        switch (state){
            case JUDGMENT:
                analyticalJudLine(line, lineIter);
                break;
            case SINGLE:
                analyticalSinLine(line, lineIter);
                break;
            case MULTIPLE:
                analyticalMulLine(line, lineIter);
                break;
        }

    }


    private void analyticalJudLine(String line, Iterator<String> lineIter){
        Pattern pattern = Pattern.compile("^(\\d+)(.*)");
        Matcher matcher = pattern.matcher(line);
        Judgment tmpJud;
        if (matcher.matches()){
            tmpJud = new Judgment();
            tmpJud.setProblem(matcher.group(2).substring(1));
            line = lineIter.next();
            while (line.charAt(0) > 'D' && line.charAt(0) < 'A'){
                if (line.charAt(1) == '.')
                    return;
                tmpJud.setProblem(tmpJud.getProblem() + line);
                line = lineIter.next();
            }
        }else return;
        tmpJud.setOption_true("正确");
        tmpJud.setOption_false("错误");
        while (!line.startsWith("参考答案")){
            line = lineIter.next();
        }
        tmpJud.setAnswer(line.substring(5).equals("正确") ? "1" : "0");
        curJud.add(tmpJud);
    }

    private void analyticalSinLine(String line, Iterator<String> lineIter){
        Pattern pattern = Pattern.compile("^(\\d+)(.*)");
        Matcher matcher = pattern.matcher(line);
        SingleChoice tmpSin;
        if (matcher.matches()){
            tmpSin = new SingleChoice();
            tmpSin.setProblem(matcher.group(2).substring(1));
            line = lineIter.next();
            while (line.charAt(0) > 'D' && line.charAt(0) < 'A'){
                if (line.charAt(1) == '.')
                    return;
                tmpSin.setProblem(tmpSin.getProblem() + line);
                line = lineIter.next();
            }
        }else return;
        tmpSin.setOption_a(line.substring(2));
        line = lineIter.next();
        tmpSin.setOption_b(line.substring(2));
        line = lineIter.next();
        tmpSin.setOption_c(line.substring(2));
        line = lineIter.next();
        tmpSin.setOption_d(line.substring(2));
        while (!line.startsWith("参考答案")){
            line = lineIter.next();
        }
        tmpSin.setAnswer(line.substring(5));
        curSingle.add(tmpSin);
    }

    private void analyticalMulLine(String line, Iterator<String> lineIter){
        Pattern pattern = Pattern.compile("^(\\d+)(.*)");
        Matcher matcher = pattern.matcher(line);
        MultipleChoice tmpMul;
        if (matcher.matches()){
            tmpMul = new MultipleChoice();
            tmpMul.setProblem(matcher.group(2).substring(1));
            line = lineIter.next();
            while (line.length() > 0 && (line.charAt(0) > 127 ||
                    (line.charAt(0) > 'D' && line.charAt(0) < 'A'))){
                if (line.charAt(1) == '.')
                    return;
                tmpMul.setProblem(tmpMul.getProblem() + line);
                line = lineIter.next();
            }
        }else return;
        while (line.equals("")){
            line = lineIter.next();
        }
        if (line.charAt(0) != 'A')
            return;
        tmpMul.setOption_a(line.substring(2));
        line = lineIter.next();
        while (line.equals("")){
            line = lineIter.next();
        }
        if (line.charAt(0) != 'B')
            return;
        tmpMul.setOption_b(line.substring(2));
        line = lineIter.next();
        while (line.equals("")){
            line = lineIter.next();
        }
        if (line.charAt(0) != 'C')
            return;
        tmpMul.setOption_c(line.substring(2));
        line = lineIter.next();
        while (line.equals("")){
            line = lineIter.next();
        }
        if (line.charAt(0) != 'D')
            return;
        tmpMul.setOption_d(line.substring(2));
        while (!line.startsWith("参考答案")){
            line = lineIter.next();
        }
        tmpMul.setAnswer(line.substring(5));
        curMultiple.add(tmpMul);
    }



    /**
     * 读取docx文件
     *
     * @param filePath 文件路径
     * @return 每一行的内容
     */
    private List<String> readDocxFile(String filePath) {
        List<String> linList = new ArrayList<>();
        String buffer;
        try {
            if (filePath.endsWith(".doc")) {
                InputStream is = new FileInputStream(filePath);
                WordExtractor ex = new WordExtractor(is);
                buffer = ex.getText();
                if (buffer.length() > 0) {
                    //使用回车换行符分割字符串
                    String[] array = buffer.split("\\r\\n");
                    for (String string : array) {
                        linList.add(string.trim());
                    }
                }
            } else if (filePath.endsWith(".docx")) {
                OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
                POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
                buffer = extractor.getText();
                extractor.close();

                if (buffer.length() > 0) {
                    //使用换行符分割字符串
                    String[] array = buffer.split("\\n");
                    for (String string : array) {
                        linList.add(string.trim());
                    }
                }
            } else {
                return null;
            }

            return linList;
        } catch (Exception e) {
            System.out.print("error---->" + filePath);
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<Judgment> getJudgments(String filename) {
        analyticalData(filename);
        return curJud;
    }

    @Override
    public List<SingleChoice> getSingleChoice(String filename) {
        analyticalData(filename);
        return curSingle;
    }

    @Override
    public List<MultipleChoice> getMultipleChoice(String filename) {
        analyticalData(filename);
        return curMultiple;
    }
}
