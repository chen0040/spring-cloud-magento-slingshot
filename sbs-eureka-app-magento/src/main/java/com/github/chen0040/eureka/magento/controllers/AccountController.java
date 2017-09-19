package com.github.chen0040.eureka.magento.controllers;


import com.github.chen0040.commons.models.MagentoAuthentication;
import com.github.chen0040.commons.utils.StringUtils;
import com.github.chen0040.eureka.magento.services.MagentoEventService;
import com.github.chen0040.magento.MagentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by xschen on 18/9/2017.
 */
@Controller
public class AccountController {

   private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

   @Value("${magento.site.url}")
   private String magentoSiteUrl;

   @Autowired
   private MagentoEventService eventService;

   @RequestMapping(value="/magento/login/client", method = RequestMethod.POST)
   public @ResponseBody MagentoAuthentication loginAsClient(@RequestParam("username") String username, @RequestParam("password") String password) {
      String token = new MagentoClient(magentoSiteUrl).loginAsClient(username, password);
      logger.info("login token: {}", token);
      if(!StringUtils.isEmpty(token)) {
         eventService.sendMessage("client login success", "authentication", "return token " + token);
         return new MagentoAuthentication(username, token, true);
      }

      eventService.sendMessage("login failed", "authentication", "return blank token");
      return new MagentoAuthentication(username, "", false);
   }

   @RequestMapping(value="/magento/login/admin", method = RequestMethod.POST)
   public @ResponseBody MagentoAuthentication loginAsAdmin(@RequestParam("username") String username, @RequestParam("password") String password) {
      String token = new MagentoClient(magentoSiteUrl).loginAsAdmin(username, password);
      logger.info("login token: {}", token);
      if(!StringUtils.isEmpty(token)) {
         eventService.sendMessage("admin login success", "authentication", "return token " + token);
         return new MagentoAuthentication(username, token, true);
      }

      eventService.sendMessage("admin login failed", "authentication", "return blank token");
      return new MagentoAuthentication(username, "", false);
   }
}
