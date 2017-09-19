package com.github.chen0040.eureka.magento.services;


/**
 * Created by xschen on 18/9/2017.
 */
public interface MagentoEventService {
   void sendMessage(String category, String name, String summary);
}
