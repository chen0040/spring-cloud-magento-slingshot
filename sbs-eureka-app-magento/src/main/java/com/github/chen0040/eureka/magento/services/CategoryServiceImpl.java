package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.eureka.magento.repositories.CategoryCache;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by xschen on 18/9/2017.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

   private Category rootCategory = null;

   private Map<Long, Category> categories = new HashMap<>();

   @Autowired
   private CategoryCache categoryCache;

   @Autowired
   private MagentoClient magentoClient;


   @Override
   public Category getRootCategory() {
      if(rootCategory == null) {
         rootCategory = magentoClient.categories().all();
         categories.clear();
         collect(rootCategory);
      }
      return rootCategory;
   }

   private void collect(Category x){
      categories.put(x.getId(), x);
      for(Category c : x.getChildren_data()){
         collect(c);
      }
   }

   @Override
   public List<Category> getTopCategories() {
      return getRootCategory().getChildren_data();
   }


   @Override public Category getCategoryById(long categoryId) {
      if(rootCategory == null) {
         getRootCategory();
      }
      return categories.get(categoryId);
   }

   @Override public long getParentCategoryId(long categoryId) {
      Category c = getCategoryById(categoryId);
      return c.getParent_id();
   }

   @Override public boolean addProductToCategory(String sku, long categoryId) {
      boolean added = magentoClient.categories().addProductToCategory(categoryId, sku, 1);
      return added;
   }

   @Override public boolean removeProductFromCategory(String sku, long categoryId) {
      return magentoClient.categories().removeProductFromCategory(categoryId, sku);
   }


   @Override public void updateCategoryTree() {
      rootCategory = null;
      getRootCategory();
   }

   @Override public void updateCategory(long categoryId) {
      categoryCache.removeProductsInCategory(categoryId);
      getProductsInCategory(categoryId);
   }


   @Override public List<CategoryProduct> getProductsInCategory(long categoryId) {
      List<CategoryProduct> result = categoryCache.getProductsInCategory(categoryId);
      if(result == null) {
         result = magentoClient.categories().getProductsInCategory(categoryId);
         categoryCache.setProductsInCategory(categoryId, result);
      }
      return result;
   }

   @Override public List<Category> findBySku(String sku) {
      if(rootCategory == null) {
         getRootCategory();
      }
      List<Category> result = new ArrayList<>();
      for(Map.Entry<Long, Category> entry : categories.entrySet()) {
         List<CategoryProduct> products = getProductsInCategory(entry.getKey());
         boolean found = false;
         for(CategoryProduct product : products) {
            if(product.getSku().equals(sku)) {
               found =true;
               break;
            }
         }
         if(found) {
            Category c = new Category();
            c.setId(entry.getValue().getId());
            c.setName(entry.getValue().getName());
            result.add(c);
         }
      }
      return result;
   }



}
