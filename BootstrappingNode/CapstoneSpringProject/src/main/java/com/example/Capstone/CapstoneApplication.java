package com.example.Capstone;

import org.example.Command.InitializeNodeCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")

public class CapstoneApplication {
    static Environment environment;

    @Autowired
    public CapstoneApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(CapstoneApplication.class, args);
        new InitializeNodeCommand(Integer.parseInt(environment.getProperty("server.port"))+1).execute();

    }

}
