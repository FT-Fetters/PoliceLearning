package com.lyun.policelearning;

import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class PoliceLearningApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void testJSON(){
        JSONArray jsonArray = new JSONArray();
        String keyword = jsonArray.toJSONString();
        System.out.println(keyword);
        System.out.println("66");
    }
}
