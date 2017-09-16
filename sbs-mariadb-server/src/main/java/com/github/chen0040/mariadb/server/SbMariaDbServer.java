package com.github.chen0040.mariadb.server;


import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static spark.Spark.get;
import static spark.Spark.port;


/**
 * Created by xschen on 12/4/2017.
 */
public class SbMariaDbServer {

   private static final Logger logger = LoggerFactory.getLogger(SbMariaDbServer.class);

   public static void main(String[] args) throws ManagedProcessException, IOException {

      File dbFile = new File("/home/sbs/mariadb");
      if(!dbFile.exists()){
         boolean created = dbFile.mkdir();
         if(!created){
            logger.error("Failed to create mariadb dir");
            return;
         }
      }

      final String sqlFilePath = "/home/sbs/mariadb/db-init.sql";

      File dbInitFile = new File(sqlFilePath);

      if(!dbInitFile.exists()){
         InputStream inStream = MariaDbUtil.getResource("db-init.sql");

         FileOutputStream outStream = new FileOutputStream(new File(sqlFilePath));

         byte[] buffer = new byte[1024];

         int length;
         while ((length = inStream.read(buffer)) > 0){
            outStream.write(buffer, 0, length);
         }

         inStream.close();
         outStream.close();
      }

      DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();
      configBuilder.setPort(3306); // OR, default: setPort(0); => autom. detect free port
      configBuilder.setDataDir("/home/sbs/mariadb"); // just an example
      DB db = DB.newEmbeddedDB(configBuilder.build());

      port(3088);

      get("/kill", (req, res) -> {
         new Thread(()->{
            try {
               Thread.sleep(100);
            }
            catch (InterruptedException e) {
               e.printStackTrace();
            }
            System.exit(0);
         }).start();

         return "mariadb will be killed in 100 milliseconds";
      });

      get("/ping", (req, res) -> "sbs-mariadb-server");

      db.start();

      db.source("db-init.sql");


   }
}
