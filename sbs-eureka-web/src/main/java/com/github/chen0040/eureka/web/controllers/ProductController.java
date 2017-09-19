package com.github.chen0040.eureka.web.controllers;


import com.github.chen0040.commons.viewmodels.ProductViewModel;
import com.github.chen0040.eureka.web.api.ProductApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by xschen on 18/9/2017.
 */
@RestController
public class ProductController {

   @Autowired
   private ProductApi productApi;

   @RequestMapping(value="/magento/products/{sku}", method = RequestMethod.GET)
   public @ResponseBody ProductViewModel getProductBySku(@PathVariable(value = "sku") String sku) {
      return productApi.getProductBySku(sku);
   }

   @RequestMapping(value="/magento/products", method = RequestMethod.POST,  consumes="application/json")
   @ResponseBody ProductViewModel saveProduct(@RequestBody ProductViewModel product) {
      return productApi.saveProduct(product);
   }

   @RequestMapping(value="/magento/products/{sku}", method = RequestMethod.DELETE)
   public @ResponseBody String deleteProduct(@PathVariable  String sku) {
      return productApi.deleteProduct(sku);
   }

   @RequestMapping(value="/magento/new-product", method = RequestMethod.GET)
   public @ResponseBody ProductViewModel newProduct(@RequestParam("username") String username) {
      return productApi.newProduct(username);
   }
}
