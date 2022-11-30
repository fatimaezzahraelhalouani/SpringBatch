package com.example.springbatchtp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.springbatchtp.configuration","com.example.springbatchtp.controller","com.example.springbatchtp.dto","com.example.springbatchtp.entities","com.example.springbatchtp.repositories"})
public class SpringBatchTpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchTpApplication.class, args);
    }


}
