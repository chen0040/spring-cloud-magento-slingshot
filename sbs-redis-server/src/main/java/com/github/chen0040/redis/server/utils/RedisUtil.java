package com.github.chen0040.redis.server.utils;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * Created by xschen on 8/4/2017.
 */
public class RedisUtil {

   public static InputStream getResource(String filename) throws IOException {
      ClassLoader classLoader = RedisUtil.class.getClassLoader();
      URL dataFile = classLoader.getResource(filename);
      return dataFile.openStream();
   }
}
