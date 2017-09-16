package com.github.chen0040.redis.server.components;


import com.github.chen0040.redis.server.services.VersionService;
import com.github.chen0040.redis.server.utils.RedisUtil;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;


/**
 * Created by xschen on 8/4/2017.
 */
@Component
public class Loader implements ApplicationListener<ApplicationReadyEvent> {

   @Autowired
   private VersionService versionService;

   private RedisServer redisServer = null;
   private Process process = null;

   private static final Logger logger = LoggerFactory.getLogger(Loader.class);

   @Override public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
      logger.info("profile: {}", versionService.getProfileString());



      if(versionService.isDefaultProfile()) {
         boolean windowsOS = versionService.isWindowsOS();
         if (windowsOS) {

            // for test only on windows OS
            String hadoop_dir = System.getProperty("hadoop.home.dir");
            if (hadoop_dir == null) {
               File tmpDir = new File("C:/tmp");
               if(!tmpDir.exists()){
                  tmpDir.mkdir();
                  tmpDir.setWritable(true);
               }

               File binDir = new File("C:/tmp/redis");
               if(!binDir.exists()) {
                  binDir.mkdir();
                  binDir.setWritable(true);

                  String zipFileName = "C:/tmp/redis/Redis-x64-3.2.100.zip";
                  try {

                     InputStream inStream = RedisUtil.getResource("Redis-x64-3.2.100.zip");

                     FileOutputStream outStream = new FileOutputStream(new File(zipFileName));

                     byte[] buffer = new byte[1024];

                     int length;
                     while ((length = inStream.read(buffer)) > 0){
                        outStream.write(buffer, 0, length);
                     }

                     inStream.close();
                     outStream.close();

                     ZipFile zipFile = new ZipFile(zipFileName);
                     zipFile.extractAll("C:/tmp/redis");
                  }
                  catch (IOException e) {
                     logger.error("Failed to copy the dict.zip from resources to C:/tmp/redis", e);
                  }
                  catch (ZipException e) {
                     logger.error("Failed to unzip " + zipFileName, e);
                  }
               }
            }
         }

         logger.info("starting local redis server ...");

         try {
            if(windowsOS) {
               //redisServer = new RedisServer("C:/tmp/redis/redis-server.exe", 6379);
                process = Runtime.getRuntime().exec("C:/tmp/redis/redis-server.exe C:/tmp/redis/redis.windows.conf");
            } else {
               redisServer = new RedisServer(6379);
               redisServer.start();
            }



            logger.info("local redis server started ...");
         }
         catch (IOException | URISyntaxException e) {
            logger.error("Failed to start local redis server", e);
         }
      }

   }

   @PreDestroy
   public void stopRedis(){
      if(versionService.isDefaultProfile()) {
         if(versionService.isWindowsOS()){
            if(process != null) {
               try {
                  process.destroy();
               } catch(Exception e) {
                  e.printStackTrace();
               } finally {
                  process = null;
               }
            }
         } else {
            if (redisServer != null) {
               try {
                  redisServer.stop();
               }
               catch (InterruptedException e) {
                  logger.error("Failed to stop redis server", e);
               } finally {
                  redisServer = null;
               }
            }
         }
      }
   }
}
