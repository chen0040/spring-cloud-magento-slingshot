package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.commons.models.MagentoEvent;

import java.util.Map;


/**
 * Created by xschen on 18/9/2017.
 */
public interface MagentoEventService {
   void sendMessage(String category, String name, String summary);

   void sendError(String category, String name, String summary);
   void sendWarning(String category, String name, String summary);

   MagentoEvent getLastEvent();

   Map<String, Long> getEventCountHistogram();

   long getCounter();
}
