package com.github.chen0040.eureka.magento.controllers;


import com.github.chen0040.eureka.magento.components.SpringRequestHelper;
import com.github.chen0040.eureka.magento.services.MagentoService;
import com.github.chen0040.eureka.magento.viewmodels.ProductViewModel;
import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;
import com.github.chen0040.magento.models.Product;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;


/**
 * Created by xschen on 13/6/2017.
 */
@RestController
public class MagentoController {

   private static final Logger logger = LoggerFactory.getLogger(MagentoController.class);

   @Autowired
   private MagentoService service;

   @Autowired
   private SpringRequestHelper requestHelper;

   private ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

   @RequestMapping(value="/magento/categories", method = RequestMethod.GET)
   public @ResponseBody List<Category> getCategories() {
      return service.getTopCategories();
   }


   @RequestMapping(value="/magento/new-product", method = RequestMethod.GET)
   public @ResponseBody Product newProduct() {
      return new Product();
   }

   @RequestMapping(value="/magento/upload/product-image-{imageType}/{sku}", method = RequestMethod.POST)
   public @ResponseBody Map<String, Object> uploadCompanyIcon(
           @PathVariable("sku") String sku,
           @PathVariable("imageType") String imageType,
           @RequestParam("file") MultipartFile file,
           HttpServletRequest request) {

      logger.info("/magento/upload/product-image-{}/{} invoked.", imageType, sku);

      try {
         byte[] bytes = file.getBytes();
         logger.info("icon bytes received: {}", bytes.length);

         long id = -1;
         if(imageType.equals("png")) {
            id = service.uploadProductImagePng(sku, bytes);
         } else {
            id = service.uploadProductImageJpeg(sku, bytes);
         }

         Map<String, Object> result = new HashMap<>();
         result.put("success", true);
         result.put("id", id);

         return result;
      }catch(IOException ex) {
         logger.error("Failed to process the uploaded icon", ex);
         Map<String, Object> result = new HashMap<>();
         result.put("success", false);
         result.put("reason", ex.getMessage());
         return result;
      }
   }

   @RequestMapping(value="/magento/add-product-to-category", method = RequestMethod.GET)
   public @ResponseBody Boolean addProductToCategory(@RequestParam("sku") String sku, @RequestParam("categoryId") long categoryId){
      return service.addProductToCategory(sku, categoryId);
   }

   @RequestMapping(value="/magento/categories/{categoryId}", method = RequestMethod.GET)
   public @ResponseBody Category getCategory(@PathVariable("categoryId") long categoryId) {
      return service.getCategoryById(categoryId);
   }

   @RequestMapping(value="/magento/categories/{categoryId}/products", method = RequestMethod.GET)
   public @ResponseBody List<CategoryProduct> getProductInCategories(@PathVariable("categoryId") long categoryId) {
      return service.getProductsInCategory(categoryId);
   }

   @RequestMapping(value="/magento/search", method = RequestMethod.GET)
   public @ResponseBody List<CategoryProduct> search(@RequestParam("keyword") String keyword) {
      return service.search(keyword);
   }

   @RequestMapping(value="/magento/products", method = RequestMethod.GET)
   public @ResponseBody List<CategoryProduct> getProducts(){
      return service.getProductsInCategory(service.getRootCategory().getId());
   }

   @RequestMapping(value="/magento/products", method = RequestMethod.POST,  consumes="application/json")
   public @ResponseBody Product saveProduct(@RequestBody Product product) {
      final Product p = service.saveProduct(product);

      return p;
   }


   @RequestMapping(value="/magento/products/{sku}", method = RequestMethod.GET)
   public @ResponseBody ProductViewModel getProduct(@PathVariable  String sku) {
      Product p = service.getProduct(sku);

      if(p == null){
         return null;
      }

      ProductViewModel viewModel = new ProductViewModel(p);

      String url = service.getProductImageUrl(p);

      viewModel.setImageUrl(url);
      return viewModel;
   }

   @RequestMapping(value="/magento/products/{sku}", method = RequestMethod.DELETE)
   public @ResponseBody String deleteProduct(@PathVariable  String sku) {
      return service.deleteProduct(sku);
   }

}
