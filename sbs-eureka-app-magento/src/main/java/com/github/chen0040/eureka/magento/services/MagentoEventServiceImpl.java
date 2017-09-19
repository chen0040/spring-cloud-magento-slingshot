package com.github.chen0040.eureka.magento.services;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.commons.models.MagentoEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * Created by xschen on 18/9/2017.
 */
@Service
public class MagentoEventServiceImpl implements MagentoEventService {

   @Autowired
   private SimpMessagingTemplate brokerMessagingTemplate;

   private int counter = 1;
   public void sendMessage(String category, String name, String description) {
      MagentoEvent magentoEvent = new MagentoEvent();
      magentoEvent.setCategory(category);
      magentoEvent.setDescription(description);
      magentoEvent.setName(name);
      magentoEvent.setTime(new Date());
      magentoEvent.setCount(counter++);

      brokerMessagingTemplate.convertAndSend("/topics//event", JSON.toJSONString(magentoEvent, SerializerFeature.BrowserCompatible));
   }
}
