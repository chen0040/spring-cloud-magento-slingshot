package com.github.chen0040.eureka.magento.repositories;


import com.github.chen0040.magento.models.CategoryProduct;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by xschen on 18/9/2017.
 */
@Repository
public class CategoryCache {

   private final Map<Long, List<CategoryProduct>> productsInCategory = new ConcurrentHashMap<>();

   public List<CategoryProduct> getProductsInCategory(long categoryId) {
      return productsInCategory.getOrDefault(categoryId, null);
   }


   public void setProductsInCategory(long categoryId, List<CategoryProduct> result) {
      productsInCategory.put(categoryId, result);
   }



   public void clearProductsInCategory() {
      productsInCategory.clear();
   }



   public void removeProductsInCategory(long categoryId) {
      productsInCategory.remove(categoryId);
   }

}
