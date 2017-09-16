package com.github.chen0040.eureka.magento.configs;

import com.github.chen0040.magento.MagentoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created by xschen on 12/27/16.
 */
@Configuration
public class BeanConfig {
   @Value("${magento.site.url}")
   private String magentoSiteUrl;

   @Bean
   public MagentoClient magentoClient(){
      MagentoClient magentoClient = new MagentoClient(magentoSiteUrl);
      return magentoClient;
   }
}
