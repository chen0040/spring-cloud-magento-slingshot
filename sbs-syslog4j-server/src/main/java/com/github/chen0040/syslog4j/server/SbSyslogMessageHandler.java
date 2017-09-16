package com.github.chen0040.syslog4j.server;


import com.github.chen0040.commons.messages.SbSyslogMessage;
import com.github.chen0040.commons.utils.StringUtils;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by xschen on 31/12/2016.
 */
public class SbSyslogMessageHandler implements SyslogServerEventHandlerIF {

   private static final Logger logger_default = LoggerFactory.getLogger(SbSyslogMessageHandler.class);

   private static final Logger logger_sbs_eureka_web = LoggerFactory.getLogger("sbs-eureka-web");
   private static final Logger logger_sbs_eureka_server = LoggerFactory.getLogger("sbs-eureka-server");
   private static final Logger logger_sbs_eureka_app = LoggerFactory.getLogger("sbs-eureka-app");
   private static final Logger logger_sbs_eureka_app_magento = LoggerFactory.getLogger("sbs-eureka-app-magento");

   private final Map<String, Logger> loggers = new HashMap<>();
   private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   private final Map<String, Queue<SbSyslogMessage>> snapshots = new HashMap<>();

   public List<String> getLoggerNames() {
      return loggers.keySet().stream().collect(Collectors.toList());
   }

   public SbSyslogMessageHandler(){
      loggers.put("sbs-eureka-web", logger_sbs_eureka_web);
      loggers.put("sbs-eureka-server", logger_sbs_eureka_server);
      loggers.put("sbs-eureka-app", logger_sbs_eureka_app);
      loggers.put("sbs-eureka-app-magento", logger_sbs_eureka_app_magento);

      for(String name : loggers.keySet()) {
         snapshots.put(name, new LinkedList<>());
      }
   }

   @Override public void event(SyslogServerIF syslogServer, SyslogServerEventIF event) {

      SbSyslogMessage message = new SbSyslogMessage();
      message.setDate(event.getDate());
      message.setFacility(event.getFacility());
      message.setLevel(event.getLevel());
      message.setHost(event.getHost());


      //logger.info(message.getMessage());
      String content = event.getMessage().trim();


      int index = content.indexOf(' ');
      String hostname = "";
      String process = "";
      String logLevel = "";
      if(index != -1) {
         hostname = content.substring(0, index);
         content = content.substring(index+1);

         index = content.indexOf(':');

         if(index != -1) {
            process = content.substring(0, index);
            content = content.substring(index+2);
         }

         index = content.indexOf(' ');

         if(index != -1) {
            logLevel = content.substring(0, index);
            content = content.substring(index+1);
         }
      }

      message.setMessage(content);
      message.setHostName(hostname);
      message.setLogLevel(logLevel);
      message.setProcess(process);

      if(!StringUtils.isEmpty(process)){
         if(loggers.containsKey(process)){
            message.setMessageId(UUID.randomUUID().toString());
            Queue<SbSyslogMessage> queue = snapshots.get(process);
            queue.add(message);
            if(queue.size() > 500) {
               queue.remove();
            }


            Logger logger = loggers.get(process);
            logger.info("{} {} [{}({})]:{} - {}", dateFormat.format(message.getDate()), process, hostname, message.getHost(), logLevel, content);
         } else {
            logger_default.info("({}) {}", message.getHost(), message.getMessage());
         }
      } else {
         logger_default.info("({}) {}", message.getHost(), message.getMessage());
      }





   }


   public List<SbSyslogMessage> getSnapshots(String name) {
      if(snapshots.containsKey(name)){
         return snapshots.get(name).stream().collect(Collectors.toList());
      } else {
         return new ArrayList<>();
      }
   }

   public String formatDate(Date d) {
      return dateFormat.format(d);
   }
}
