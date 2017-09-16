package com.github.chen0040.eureka.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


/**
 * Created by xschen on 14/9/2017.
 */
@SpringBootApplication
@EnableEurekaClient
public class SbApplication {

   public static void main(String[] args) {
      SpringApplication.run(SbApplication.class, args);
   }


}
