package com.github.chen0040.eureka.magento.services;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.commons.models.MagentoEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Created by xschen on 18/9/2017.
 */
@Service
public class MagentoEventServiceImpl implements MagentoEventService {

   private ConcurrentMap<String, Long> histogram = new ConcurrentHashMap<>();

   private MagentoEvent lastEvent = new MagentoEvent();

   @Autowired
   private SimpMessagingTemplate brokerMessagingTemplate;

   private long counter = 1;

   @Override
   public void sendMessage(String category, String name, String description) {
      MagentoEvent magentoEvent = new MagentoEvent();
      magentoEvent.setCategory(category);
      magentoEvent.setDescription(description);
      magentoEvent.setName(name);
      magentoEvent.setTime(new Date());
      magentoEvent.setCount(counter++);

      histogram.put(category, histogram.getOrDefault(category, 0L) + 1);

      lastEvent = magentoEvent;

      brokerMessagingTemplate.convertAndSend("/topics/event", JSON.toJSONString(magentoEvent, SerializerFeature.BrowserCompatible));
   }

   @Override
   public void sendError(String category, String name, String description) {
      MagentoEvent magentoEvent = new MagentoEvent();
      magentoEvent.setCategory(category);
      magentoEvent.setDescription(description);
      magentoEvent.setName(name);
      magentoEvent.setTime(new Date());
      magentoEvent.setCount(counter++);
      magentoEvent.setLevel("Error");

      histogram.put(category, histogram.getOrDefault(category, 0L) + 1);

      lastEvent = magentoEvent;

      brokerMessagingTemplate.convertAndSend("/topics/event", JSON.toJSONString(magentoEvent, SerializerFeature.BrowserCompatible));
   }

   @Override
   public void sendWarning(String category, String name, String description) {
      MagentoEvent magentoEvent = new MagentoEvent();
      magentoEvent.setCategory(category);
      magentoEvent.setDescription(description);
      magentoEvent.setName(name);
      magentoEvent.setTime(new Date());
      magentoEvent.setCount(counter++);
      magentoEvent.setLevel("Warning");

      histogram.put(category, histogram.getOrDefault(category, 0L) + 1);

      lastEvent = magentoEvent;

      brokerMessagingTemplate.convertAndSend("/topics/event", JSON.toJSONString(magentoEvent, SerializerFeature.BrowserCompatible));
   }

   @Override public MagentoEvent getLastEvent() {
      return lastEvent;
   }


   @Override public Map<String, Long> getEventCountHistogram() {
      return histogram;
   }

   @Override public long getCounter() {
      return counter;
   }
}
