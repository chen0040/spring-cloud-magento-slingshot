package com.github.chen0040.mariadb.server;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * Created by xschen on 8/4/2017.
 */
public class MariaDbUtil {

   public static InputStream getResource(String filename) throws IOException {
      ClassLoader classLoader = MariaDbUtil.class.getClassLoader();
      URL dataFile = classLoader.getResource(filename);
      return dataFile.openStream();
   }
}
