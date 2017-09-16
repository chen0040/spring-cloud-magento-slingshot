package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;
import com.github.chen0040.magento.models.Product;

import java.util.List;


/**
 * Created by xschen on 13/6/2017.
 */
public interface MagentoService {
   List<Category> getTopCategories();
   Category getCategoryById(long categoryId);
   Category getRootCategory();

   List<CategoryProduct> getProductsInCategory(long categoryId);
   List<CategoryProduct> search(String keyword);

   Product getProduct(String sku);
   Product saveProduct(Product product);
   String deleteProduct(String sku);

   String getProductImageUrl(String sku);
   String getProductImageUrl(Product p);

   long uploadProductImagePng(String sku, byte[] bytes);
   long uploadProductImageJpeg(String sku, byte[] bytes);

   boolean addProductToCategory(String sku, long categoryId);

   byte[] getProductImage(String sku);

   long getParentCategoryId(long categoryId);

   void downloadImage(String sku);
}
