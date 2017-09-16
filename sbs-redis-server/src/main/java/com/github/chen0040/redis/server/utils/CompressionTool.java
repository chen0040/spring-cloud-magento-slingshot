package com.github.chen0040.redis.server.utils;

/**
 * Created by xschen on 16/4/2017.
 */
public interface CompressionTool {

   void unzip(String src, String dest, String password);

   void zip(String src, String dest, String password);
}
