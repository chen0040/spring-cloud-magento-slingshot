package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;

import java.util.List;


/**
 * Created by xschen on 18/9/2017.
 */
public interface CategoryService {
   List<Category> getTopCategories();
   Category getCategoryById(long categoryId);
   Category getRootCategory();

   List<CategoryProduct> getProductsInCategory(long categoryId);

   long getParentCategoryId(long categoryId);

   boolean addProductToCategory(String sku, long categoryId);

   void invalidateRootCategory();
}
