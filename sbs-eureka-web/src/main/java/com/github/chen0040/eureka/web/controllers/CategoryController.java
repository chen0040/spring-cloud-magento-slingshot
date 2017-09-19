package com.github.chen0040.eureka.web.controllers;


import com.github.chen0040.eureka.web.api.CategoryApi;
import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by xschen on 18/9/2017.
 */
@RestController
public class CategoryController {
   @Autowired
   private CategoryApi categoryApi;

   @RequestMapping(value="/magento/categories", method = RequestMethod.GET)
   public @ResponseBody List<Category> getCategories() {
      return categoryApi.getCategories();
   }

   @RequestMapping(value="/magento/root-category", method = RequestMethod.GET)
   public @ResponseBody Category getRootCategory() {
      return categoryApi.getRootCategory();
   }

   @RequestMapping(value="/magento/categories/{categoryId}", method = RequestMethod.GET)
   public @ResponseBody Category getCategory(@PathVariable("categoryId") long categoryId) {
      return categoryApi.getCategoryById(categoryId);
   }

   @RequestMapping(value="/magento/categories/{categoryId}/products", method = RequestMethod.GET)
   public @ResponseBody List<CategoryProduct> getProductsInCategory(@PathVariable("categoryId") long categoryId) {
      return categoryApi.getProductsInCategory(categoryId);
   }

   @RequestMapping(value="/magento/products", method = RequestMethod.GET)
   public @ResponseBody List<CategoryProduct> getProductsInAllCategories() {
      return categoryApi.getProductsInAllCategories();
   }

   @RequestMapping(value="/magento/add-product-to-category", method = RequestMethod.GET)
   public @ResponseBody Boolean addProductToCategory(@RequestParam("sku") String sku, @RequestParam("categoryId") long categoryId) {
      return categoryApi.addProductToCategory(sku, categoryId);
   }
}
