package com.github.chen0040.eureka.magento.repositories;


import com.github.chen0040.magento.models.Product;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by xschen on 9/7/2017.
 */
@Repository
public class ProductCache {

   private final Map<String, Product> products = new ConcurrentHashMap<>();


   public Product getProduct(String sku) {
      return products.getOrDefault(sku, null);
   }


   public void setProduct(String sku, Product p) {
      products.put(sku, p);
   }

   public void removeProduct(String sku) {
      products.remove(sku);
   }




}
