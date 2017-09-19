package com.github.chen0040.eureka.web.controllers;


import com.github.chen0040.eureka.web.components.MagentoSession;
import com.github.chen0040.eureka.web.components.SpringAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by xschen on 18/9/2017.
 */
@Controller
public class UserController {
   @Autowired
   private SpringAuthentication authentication;

   @Autowired
   private MagentoSession magentoSession;

   @RequestMapping(value="/magento/identity", method= RequestMethod.GET)
   public @ResponseBody Map<String, Object> getIdentity(HttpServletRequest request) {
      Map<String, Object> result = new HashMap<>();
      String username = magentoSession.getUsername(request);
      result.put("username", username);
      result.put("authenticated", authentication.isAuthenticated());
      return result;
   }
}
