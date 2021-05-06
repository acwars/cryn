package com.onlinejudge.cryn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.onlinejudge.cryn.dao")
@EnableTransactionManagement
@ServletComponentScan
public class CrynApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrynApplication.class, args);
    }

}
