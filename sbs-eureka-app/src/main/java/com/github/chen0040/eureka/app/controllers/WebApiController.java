package com.github.chen0040.eureka.app.controllers;


import com.github.chen0040.commons.messages.Greeting;
import org.springframework.web.bind.annotation.*;


/**
 * Created by xschen on 14/9/2017.
 */
@RestController
public class WebApiController {
   @RequestMapping(value="greeting/{name}", method= RequestMethod.GET)
   @ResponseBody Greeting greeting(@PathVariable("name") String name) {
      return new Greeting(name);
   }
}
