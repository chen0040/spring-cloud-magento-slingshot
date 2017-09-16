package com.github.chen0040.syslog4j.server;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.commons.messages.SbSyslogMessage;
import org.productivity.java.syslog4j.server.SyslogServer;
import org.productivity.java.syslog4j.server.SyslogServerConfigIF;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.impl.event.printstream.FileSyslogServerEventHandler;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.port;


/**
 * Created by xschen on 14/9/2017.
 */
public class SbSyslogServer {
   private static String protocol = "udp"; //"tcp";
   private static String host = "0.0.0.0";
   private static int syslog_port = 8089;
   private static boolean append = true;
   private static String fileName = null;

   private static SbSyslogMessageHandler eventHandlerIF;

   public static void main(String[] args) throws Exception {
      System.out.println("SyslogServer " + SyslogServer.getVersion());

      if (!SyslogServer.exists(protocol)) {
         System.out.println("Protocol \"" + protocol + "\" not supported");
         System.exit(1);
      }

      SyslogServerIF syslogServer = SyslogServer.getInstance(protocol);

      SyslogServerConfigIF syslogServerConfig = syslogServer.getConfig();

      /*
      if (host != null) {
         syslogServerConfig.setHost(host);
         System.out.println("Listening on host: " + host);
      }*/

      syslogServerConfig.setPort(syslog_port);

      if (fileName != null) {
         SyslogServerEventHandlerIF eventHandler = new FileSyslogServerEventHandler(fileName,append);
         syslogServerConfig.addEventHandler(eventHandler);
         System.out.println((append ? "Appending" : "Writing") + " to file: " + fileName);
      }

      /*
      SyslogServerEventHandlerIF eventHandler = SystemOutSyslogServerEventHandler.create();
      syslogServerConfig.addEventHandler(eventHandler);
      */

      eventHandlerIF = new SbSyslogMessageHandler();
      syslogServerConfig.addEventHandler(eventHandlerIF);

      System.out.println();

      SyslogServer.getThreadedInstance(protocol);

      port(8088);



      get("/", (req, res) -> {
         StringBuilder sb = new StringBuilder();
         for(String name : eventHandlerIF.getLoggerNames()){
            sb.append("<li><a href=\"/html/").append(name).append("\">").append(name).append("</a></li>");
         }
         String html = sb.toString();
         return html;
      });

      get("/kill", (req, res) -> {
         new Thread(()->{
            try {
               Thread.sleep(100);
            }
            catch (InterruptedException e) {
               e.printStackTrace();
            }
            System.exit(0);
         });

         return "mariadb will be killed in 100 milliseconds";
      });

      get("/ping", (req, res) -> "sbs-syslog4j-server");

      for(String name : eventHandlerIF.getLoggerNames()) {
         get("/html/" + name, (request, response) -> {
            List<SbSyslogMessage> messages = eventHandlerIF.getSnapshots(name);
            StringBuilder sb = new StringBuilder();
            sb.append("<a href=\"/\">HOME</a>");
            for(String loggerName : eventHandlerIF.getLoggerNames()){
               sb.append("&nbsp;<a href=\"/html/").append(loggerName).append("\">").append(loggerName).append("</a></li>");
            }

            sb.append("<hr />");
            sb.append("<table style=\"width:100%\">");
            sb.append("<tr><td>Time</td><td>Hostname</td><td>ip</td><td>process</td><td>logLevel</td><td>message</td>");
            for(int i=0; i < messages.size(); ++i) {
               SbSyslogMessage message =messages.get(i);
               sb.append("<tr>");
               sb.append("<td>").append(eventHandlerIF.formatDate(message.getDate())).append("</td>");
               sb.append("<td>").append(message.getHostName()).append("</td>");
               sb.append("<td>").append(message.getHost()).append("</td>");
               sb.append("<td>").append(message.getProcess()).append("</td>");
               sb.append("<td>").append(message.getLogLevel()).append("</td>");
               sb.append("<td>").append(message.getMessage()).append("</td>");
               sb.append("</tr>");
            }
            sb.append("</table>");

            return sb.toString();
         });

         get("/json/" + name, "application/json", (request, response) -> {
            return eventHandlerIF.getSnapshots(name);
         }, obj -> JSON.toJSONString(obj, SerializerFeature.BrowserCompatible));
      }

   }
}
