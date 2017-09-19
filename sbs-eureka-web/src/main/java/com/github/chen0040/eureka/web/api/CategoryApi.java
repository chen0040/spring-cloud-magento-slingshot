package com.github.chen0040.eureka.web.api;


import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by xschen on 18/9/2017.
 */
@FeignClient("sbs-eureka-app-magento")
public interface CategoryApi {
   @RequestMapping(value="/magento/categories", method = RequestMethod.GET)
   @ResponseBody List<Category> getCategories();

   @RequestMapping(value="/magento/root-category", method = RequestMethod.GET)
   @ResponseBody Category getRootCategory();

   @RequestMapping(value="/magento/categories/{categoryId}", method = RequestMethod.GET)
   @ResponseBody Category getCategoryById(@PathVariable("categoryId") long categoryId);

   @RequestMapping(value="/magento/categories/{categoryId}/products", method = RequestMethod.GET)
   @ResponseBody List<CategoryProduct> getProductsInCategory(@PathVariable("categoryId") long categoryId);

   @RequestMapping(value="/magento/products", method = RequestMethod.GET)
   @ResponseBody List<CategoryProduct> getProductsInAllCategories();

   @RequestMapping(value="/magento/add-product-to-category", method = RequestMethod.GET)
   @ResponseBody Boolean addProductToCategory(@RequestParam("sku") String sku, @RequestParam("categoryId") long categoryId);
}
