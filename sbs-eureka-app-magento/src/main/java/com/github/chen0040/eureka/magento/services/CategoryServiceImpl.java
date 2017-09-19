package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.eureka.magento.repositories.CategoryRepository;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
   private CategoryRepository categoryRepository;

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

      rootCategory = null;
      categoryRepository.removeProductsInCategory(categoryId);

      return added;
   }


   @Override public void invalidateRootCategory() {
      rootCategory = null;
      categoryRepository.clearProductsInCategory();
   }


   @Override public List<CategoryProduct> getProductsInCategory(long categoryId) {
      List<CategoryProduct> result = categoryRepository.getProductsInCategory(categoryId);
      if(result == null) {
         result = magentoClient.categories().getProductsInCategory(categoryId);
         categoryRepository.setProductsInCategory(categoryId, result);
      }
      return result;
   }


}
