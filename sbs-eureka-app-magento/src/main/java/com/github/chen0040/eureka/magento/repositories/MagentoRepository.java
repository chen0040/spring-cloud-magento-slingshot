package com.github.chen0040.eureka.magento.repositories;


import com.github.chen0040.commons.utils.StringUtils;
import com.github.chen0040.magento.models.CategoryProduct;
import com.github.chen0040.magento.models.Product;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by xschen on 9/7/2017.
 */
@Repository
public class MagentoRepository {
   private final Map<Long, List<CategoryProduct>> productsInCategory = new ConcurrentHashMap<>();
   private final Map<String, Product> products = new ConcurrentHashMap<>();
   private final Map<String, String> magentoProductImageUrls = new ConcurrentHashMap<>();


   public List<CategoryProduct> getProductsInCategory(long categoryId) {
      return productsInCategory.getOrDefault(categoryId, null);
   }


   public void setProductsInCategory(long categoryId, List<CategoryProduct> result) {
      productsInCategory.put(categoryId, result);
   }


   public Product getProduct(String sku) {
      return products.getOrDefault(sku, null);
   }


   public void setProduct(String sku, Product p) {
      products.put(sku, p);
   }


   public void clearProductsInCategory() {
      productsInCategory.clear();
   }


   public void removeProduct(String sku) {
      products.remove(sku);
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


   public void removeProductsInCategory(long categoryId) {
      productsInCategory.remove(categoryId);
   }

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


}
