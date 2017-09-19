package com.github.chen0040.eureka.web.api;


import com.github.chen0040.commons.models.MagentoAuthentication;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by xschen on 18/9/2017.
 */
@FeignClient("sbs-eureka-app-magento")
public interface AccountApi {
   @RequestMapping(value="/magento/login/client", method = RequestMethod.POST)
   @ResponseBody MagentoAuthentication loginAsClient(@RequestParam("username") String username, @RequestParam("password") String password);

   @RequestMapping(value="/magento/login/admin", method = RequestMethod.POST)
   @ResponseBody MagentoAuthentication loginAsAdmin(@RequestParam("username") String username, @RequestParam("password") String password);
}
