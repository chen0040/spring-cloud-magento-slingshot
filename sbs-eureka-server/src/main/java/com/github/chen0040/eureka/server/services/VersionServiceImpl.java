package com.github.chen0040.eureka.server.services;


import com.github.chen0040.redis.server.utils.CollectionUtil;
import com.github.chen0040.redis.server.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by xschen on 18/3/2017.
 */
@Service
public class VersionServiceImpl implements VersionService {

   private static final Logger logger = LoggerFactory.getLogger(VersionServiceImpl.class);

   @Autowired
   private Environment environment;

   @Override
   public String getProfileString() {
      List<String> profiles = CollectionUtil.toList(this.environment.getActiveProfiles());
      return profiles.stream().collect(Collectors.joining("-"));

   }

   @Override
   public boolean isDefaultProfile(){
      String profile = getProfileString();
      return StringUtils.isEmpty(profile) || profile.contains("docker") || profile.contains("iss") || profile.contains("default") || profile.contains("mysql") || profile.contains("hsql");
   }


   @Override public boolean isWindowsOS() {
      String version = System.getProperty("os.name");
      if(StringUtils.isEmpty(version)){
         return false;
      }
      return version.toLowerCase().contains("win");
   }
}
