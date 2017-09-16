package com.github.chen0040.eureka.web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Created by xschen on 14/9/2017.
 */
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableEurekaClient
@EnableFeignClients
public class WebApplication {
   public static void main(String[] args) {
      SpringApplication.run(WebApplication.class, args);
   }
}
