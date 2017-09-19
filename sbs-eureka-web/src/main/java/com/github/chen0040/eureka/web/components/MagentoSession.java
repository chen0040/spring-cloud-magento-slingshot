package com.github.chen0040.eureka.web.components;


import com.github.chen0040.commons.models.MagentoCart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Created by xschen on 21/12/2016.
 */
@Component
public class MagentoSession {

   private static final List<String> supportedLanguages = Arrays.asList("en", "cn");

   private ConcurrentMap<String, MagentoCart> carts = new ConcurrentHashMap<>();

   private static final Logger logger = LoggerFactory.getLogger(MagentoSession.class);

   @Autowired
   private LocaleResolver localeResolver;

   @Autowired
   private SpringAuthentication authentication;

   public String getUserIpAddress(HttpServletRequest request) {
      String ipAddress = request.getHeader("X-FORWARDED-FOR");
      if (ipAddress == null) {
         ipAddress = request.getRemoteAddr();
      }
      return ipAddress;
   }

   public String getCartId(HttpServletRequest request) {
      String ipAddress = getUserIpAddress(request);
      String username = authentication.getUsername();

      String key = ipAddress;

      if(authentication.isAuthenticated()) {
         key = username;
      }

      if(carts.containsKey(key)) {
         return carts.get(key).getId();
      }
      return "";
   }


   public void storeCart(HttpServletRequest request, MagentoCart cart) {
      String ipAddress = getUserIpAddress(request);
      String username = authentication.getUsername();

      String key = ipAddress;

      if(authentication.isAuthenticated()) {
         key = username;
      }

      logger.info("store cart {} on {}", cart.getId(), key);

      carts.put(key, cart);
   }

   public String getLanguage(HttpServletRequest request) {
      Locale locale = RequestContextUtils.getLocale(request);
      String language = locale.getLanguage();
      if(!supportedLanguages.contains(language)){
         language = "en";
      }

      return language;
   }


   public String getUsername(HttpServletRequest request) {
      String username;
      if(authentication.isAuthenticated()) {
         username = authentication.getUsername();
      } else {
         username = getUserIpAddress(request);
      }
      return username;
   }
}
