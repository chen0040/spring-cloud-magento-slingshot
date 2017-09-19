package com.github.chen0040.commons.models;


import lombok.Getter;
import lombok.Setter;


/**
 * Created by xschen on 18/9/2017.
 */
@Getter
@Setter
public class MagentoAuthentication {
   private String username;
   private String token;
   private boolean authenticated;

   public MagentoAuthentication() {

   }

   public MagentoAuthentication(String username, String token, boolean authenticated) {
      this.username = username;
      this.token = token;
      this.authenticated = authenticated;
   }
}
