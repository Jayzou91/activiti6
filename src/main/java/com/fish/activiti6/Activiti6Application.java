package com.fish.activiti6;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.fish.activiti6.dao")
@EnableTransactionManagement
public class Activiti6Application {

    public static void main(String[] args) {
        SpringApplication.run(Activiti6Application.class, args);
    }

}
