package com.github.chen0040.eureka.web.components;

import com.github.chen0040.commons.models.SpringUser;
import com.github.chen0040.eureka.web.models.SpringUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;


/**
 * Created by xschen on 21/12/2016.
 */
@Component
public class SpringAuthentication {

   private static final Logger logger = LoggerFactory.getLogger(SpringAuthentication.class);

   public boolean hasRole(Authentication authentication, String role) {
      Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
      for(GrantedAuthority authority : authorities) {
         String authorityRole = authority.getAuthority();
         if(authorityRole.equals(role)){
            return true;
         }
      }
      return false;
   }

   public SpringUserDetails getUserDetails() {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if(principal instanceof SpringUserDetails) {
         return (SpringUserDetails) principal;
      } else {
         logger.warn("Failed to get principal as SpringUserDetails: {}", principal);
         return null;
      }
   }

   public String getUsername(){
      SpringUserDetails user = getUserDetails();
      if(user != null) {
         return user.getUsername();
      } else {
         logger.warn("Failed to obtain user");
      }
      return "";
   }


   public long getUserId(){
      SpringUserDetails user = getUserDetails();
      return user.getUserId();
   }


   public SpringUser getUser() {
      SpringUserDetails userDetails = getUserDetails();
      if(userDetails != null) {
         return userDetails.getUser();
      } else {
         return null;
      }
   }

   public boolean hasRole(String role) {
      Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
      for(GrantedAuthority authority : authorities) {
         String authorityRole = authority.getAuthority();
         logger.info("role: {}", authorityRole);
         if(authorityRole.equals(role)){
            return true;
         }
      }
      return false;
   }

   public boolean isAuthenticated(){
      return SecurityContextHolder.getContext().getAuthentication() != null &&
              SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
              //when Anonymous Authentication is enabled
              !(SecurityContextHolder.getContext().getAuthentication()
                      instanceof AnonymousAuthenticationToken);
   }


   public String getToken() {
      SpringUser user = getUser();
      if(user != null) return user.getToken();
      return "";
   }
}
