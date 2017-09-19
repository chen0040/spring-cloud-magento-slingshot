package com.github.chen0040.eureka.web.api;


import com.github.chen0040.commons.messages.Greeting;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by xschen on 13/11/2016.
 */
@FeignClient("sbs-eureka-app")
public interface SbAppClient {
   @RequestMapping(value="greeting/{name}", method= RequestMethod.GET)
   @ResponseBody Greeting greeting(@PathVariable("name") String name);

}
