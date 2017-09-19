package com.github.chen0040.eureka.web.components;


import com.github.chen0040.commons.models.MagentoAuthentication;
import com.github.chen0040.commons.models.SpringUser;
import com.github.chen0040.eureka.web.api.AccountApi;
import com.github.chen0040.eureka.web.models.SpringUserEntity;
import com.github.chen0040.eureka.web.services.SpringUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;


/**
 * Created by xschen on 18/9/2017.
 */
@Component
public class MagentoAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

   @Autowired
   private AccountApi accountApi;
   @Autowired
   private SpringUserService userService;


   private static final Logger logger = LoggerFactory.getLogger(MagentoAuthenticationFilter.class);

   public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
      String username = this.obtainUsername(request);
      String password = this.obtainPassword(request);

      MagentoAuthentication magentoAuthentication;
      if(username.equals("admin")) {
         magentoAuthentication = accountApi.loginAsAdmin(username, password);
      } else {
         magentoAuthentication = accountApi.loginAsClient(username, password);
      }

      if(magentoAuthentication.isAuthenticated()) {
         logger.info("magento account is authenticated");
         updateAccount(username, password, magentoAuthentication.getToken(), username.equals("admin"));
      } else {
         logger.warn("magento account does not match");
         preventLogin(username);
      }

      return super.attemptAuthentication(request, response);
   }

   private void updateAccount(String username, String password, String token, boolean isAdmin) {

      logger.info("Update user: {} with token {}", username, token);

      SpringUser user;
      Optional<SpringUser> userOptional = userService.findUserByUsername(username);
      if(userOptional.isPresent()){
         user = userOptional.get();
         if(isAdmin) {
            String roles = user.getRoles();
            if (!roles.contains("ROLE_ADMIN") || !roles.contains("ROLE_USER")) {
               user.setRoles("ROLE_USER,ROLE_ADMIN");
            }
         }
      } else {
         user = new SpringUserEntity();
         user.setUsername(username);
         if(isAdmin) {
            user.setRoles("ROLE_USER,ROLE_ADMIN");
         } else {
            user.setRoles("ROLE_USER");
         }

         user.setEmail("xs0040@gmail.com");
      }

      user.setToken(token);
      user.setEnabled(true);
      user.setPassword(password);

      //logger.info("Before saved: \n{}", JSON.toJSONString(user, SerializerFeature.PrettyFormat));

      user = userService.save(user);

      //logger.info("Saved: \n{}", JSON.toJSONString(user, SerializerFeature.PrettyFormat));
   }

   private void preventLogin(String username) {
      SpringUser user;
      Optional<SpringUser> userOptional = userService.findUserByUsername(username);
      if(userOptional.isPresent()){
         user = userOptional.get();
         String password = UUID.randomUUID().toString();
         user.setPassword(password);

         userService.save(user);
      }
   }

   @Override
   @Autowired
   public void setAuthenticationManager(AuthenticationManager authenticationManager) {
      super.setAuthenticationManager(authenticationManager);
   }

}
