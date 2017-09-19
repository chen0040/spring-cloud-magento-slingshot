package com.github.chen0040.eureka.magento.controllers;


import com.github.chen0040.commons.viewmodels.ProductViewModel;
import com.github.chen0040.eureka.magento.services.ProductMediaService;
import com.github.chen0040.eureka.magento.services.ProductService;
import com.github.chen0040.magento.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Created by xschen on 18/9/2017.
 */
@Controller
public class ProductController {

   @Autowired
   private ProductService productService;

   @Autowired
   private ProductMediaService productMediaService;

   @RequestMapping(value="/magento/products/{sku}", method = RequestMethod.GET)
   public @ResponseBody ProductViewModel getProductBySku(@PathVariable(value = "sku") String sku) {
      Product p = productService.getProduct(sku);
      String username = productService.getUsernameBySku(sku);

      if(p == null){
         return null;
      }

      ProductViewModel viewModel = new ProductViewModel(p, username);

      String url = productMediaService.getProductImageUrl(p);

      viewModel.setImageUrl(url);
      return viewModel;
   }

   @RequestMapping(value="/magento/products", method = RequestMethod.POST,  consumes="application/json")
   public @ResponseBody ProductViewModel saveProduct(@RequestBody ProductViewModel product) {
      productService.addProductToVendor(product, product.getUsername());
      final Product p = productService.saveProduct(product);
      return new ProductViewModel(p, product.getUsername());
   }

   @RequestMapping(value="/magento/products/{sku}", method = RequestMethod.DELETE)
   public @ResponseBody String deleteProduct(@PathVariable("sku")  String sku) {
      return productService.deleteProduct(sku);
   }

   @RequestMapping(value="/magento/new-product", method = RequestMethod.GET)
   public @ResponseBody ProductViewModel newProduct(@RequestParam("username") String username) {
      return new ProductViewModel(new Product(), username);
   }
}
