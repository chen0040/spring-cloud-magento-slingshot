package com.github.chen0040.eureka.web.controllers;


import com.github.chen0040.eureka.web.services.VersionService;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;


/**
 * Created by xschen on 13/4/2017.
 */
@Controller
public class CommandController {

   private ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

   @Autowired
   private VersionService versionService;

   @Value("${spring.application.name}")
   private String appName;

   @RequestMapping(value="/kill", method= RequestMethod.GET)
   public @ResponseBody Map<String, Object> kill(){
      Map<String, Object> result = new HashMap<>();
      if(versionService.isDefaultProfile()){
         result.put("status", "redis will be killed in 100 milliseconds");
         service.submit(()->{
            try {
               Thread.sleep(100);
            }
            catch (InterruptedException e) {
               e.printStackTrace();
            }
            System.exit(0);
         });
      }

      return result;
   }

   @RequestMapping(value="/ping", method= RequestMethod.GET)
   public @ResponseBody String ping(){
      if(versionService.isDefaultProfile()){
         return appName;
      }
      return "";
   }
}

