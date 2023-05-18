package com.example.databasefinalproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com/example/databasefinalproject/Mapper")
public class DatabaseFinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseFinalProjectApplication.class, args);
    }

}
