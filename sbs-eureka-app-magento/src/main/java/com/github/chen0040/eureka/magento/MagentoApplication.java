package com.github.chen0040.eureka.magento;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Created by xschen on 16/9/2017.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@EnableScheduling
public class MagentoApplication {

   public static void main(String[] args) {
      SpringApplication.run(MagentoApplication.class, args);
   }
}
