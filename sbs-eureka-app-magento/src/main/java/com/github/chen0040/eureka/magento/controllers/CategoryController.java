package com.github.chen0040.eureka.magento.controllers;


import com.github.chen0040.eureka.magento.services.CategoryService;
import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by xschen on 18/9/2017.
 */
@Controller
public class CategoryController {

   @Autowired
   private CategoryService categoryService;

   @RequestMapping(value="/magento/categories", method = RequestMethod.GET)
   public @ResponseBody List<Category> getCategories() {
      return categoryService.getTopCategories();
   }

   @RequestMapping(value="/magento/root-category", method = RequestMethod.GET)
   public @ResponseBody Category getRootCategory() {
      return categoryService.getRootCategory();
   }

   @RequestMapping(value="/magento/categories/{categoryId}", method = RequestMethod.GET)
   public @ResponseBody Category getCategoryById(@PathVariable("categoryId") long categoryId) {
      return categoryService.getCategoryById(categoryId);
   }

   @RequestMapping(value="/magento/categories/{categoryId}/products", method = RequestMethod.GET)
   public @ResponseBody List<CategoryProduct> getProductsInCategory(@PathVariable("categoryId") long categoryId) {
      return categoryService.getProductsInCategory(categoryId);
   }

   @RequestMapping(value="/magento/products", method = RequestMethod.GET)
   public @ResponseBody List<CategoryProduct> getProductsInAllCategories(){
      return categoryService.getProductsInCategory(categoryService.getRootCategory().getId());
   }

   @RequestMapping(value="/magento/add-product-to-category", method = RequestMethod.GET)
   public @ResponseBody Boolean addProductToCategory(@RequestParam("sku") String sku, @RequestParam("categoryId") long categoryId){
      return categoryService.addProductToCategory(sku, categoryId);
   }
}
