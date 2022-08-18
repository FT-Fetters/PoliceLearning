package com.lyun.policelearning;

import com.lyun.policelearning.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(SpringUtil.class)
@SpringBootApplication
public class PoliceLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoliceLearningApplication.class, args);
    }

}
