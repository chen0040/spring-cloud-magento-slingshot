package com.github.chen0040.eureka.magento.repositories;


import com.github.chen0040.commons.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by xschen on 18/9/2017.
 */
@Repository
public class ProductMediaRepository {

   private final Map<String, String> magentoProductImageUrls = new ConcurrentHashMap<>();

   public boolean hasProductImage(String sku) {
      String path = "/tmp/" + sku + ".img";
      return new File(path).exists();

   }

   public byte[] getProductImage(String sku){
      String path = "/tmp/" + sku + ".img";
      File f = new File(path);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try {
         InputStream fis = new FileInputStream(f);
         byte[] buffer = new byte[1024];
         int len = 0;

         while((len = fis.read(buffer, 0, 1024)) > 0){
            baos.write(buffer, 0, len);
         }
         fis.close();
         baos.close();

         return baos.toByteArray();
      }
      catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }

   public void setProductImage(String sku, byte[] bytes) {
      String path = "/tmp/" + sku + ".img";
      File f = new File(path);

      try(OutputStream outputStream = new FileOutputStream(f)){
         outputStream.write(bytes);
      }
      catch (IOException e) {
         e.printStackTrace();
      }
   }


   public String getProductImageUrl(String sku) {
      if(hasProductImage(sku)){
         return "/product-img/" + StringUtils.encodeUriComponent(sku);
      }
      return magentoProductImageUrls.getOrDefault(sku, null);
   }


   public void setMagentoProductImageUrl(String sku, String url) {
      magentoProductImageUrls.put(sku, url);
   }


   public void removeMagentoProductImageUrl(String sku) {
      magentoProductImageUrls.remove(sku);
   }


}
