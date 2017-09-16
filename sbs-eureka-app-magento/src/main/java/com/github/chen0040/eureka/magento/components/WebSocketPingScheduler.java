package com.github.chen0040.eureka.magento.components;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by xschen on 16/9/2017.
 *
 * A simple scheduler that tries to ping the web client every 10 seconds via web-socket
 */
@Component
public class WebSocketPingScheduler {

   @Autowired
   private SimpMessagingTemplate brokerMessagingTemplate;

   @Scheduled(fixedDelay = 10000L)
   public void webSocketPing() {
      Map<String, Object> ping = new HashMap<>();
      ping.put("message", "PING!");
      ping.put("time", new Date());
      brokerMessagingTemplate.convertAndSend("/topics/ping", JSON.toJSONString(ping, SerializerFeature.BrowserCompatible));
   }
}
