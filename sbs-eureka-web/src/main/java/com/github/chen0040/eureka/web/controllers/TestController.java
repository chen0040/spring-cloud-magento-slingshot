package com.github.chen0040.eureka.web.controllers;


import com.github.chen0040.commons.messages.Greeting;
import com.github.chen0040.eureka.web.api.SbAppClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by xschen on 14/9/2017.
 */
@RestController
public class TestController {

   @Autowired
   private SbAppClient appClient;

   @RequestMapping(value="greeting/{name}", method= RequestMethod.GET)
   @ResponseBody Greeting greeting(@PathVariable("name") String name) {
      return appClient.greeting(name);
   }
}
