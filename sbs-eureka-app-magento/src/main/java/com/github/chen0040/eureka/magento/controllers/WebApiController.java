package com.github.chen0040.eureka.magento.controllers;


import com.github.chen0040.commons.messages.Greeting;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by xschen on 14/9/2017.
 */
@Controller
public class WebApiController {
   @RequestMapping(value="greeting/{name}", method= RequestMethod.GET)
   @ResponseBody Greeting greeting(@PathVariable("name") String name) {
      return new Greeting(name);
   }
}
