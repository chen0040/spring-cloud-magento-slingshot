package com.github.chen0040.eureka.magento.controllers;


import com.github.chen0040.commons.utils.StringUtils;
import com.github.chen0040.commons.viewmodels.ProductViewModel;
import com.github.chen0040.eureka.magento.services.CategoryService;
import com.github.chen0040.eureka.magento.services.MagentoEventService;
import com.github.chen0040.eureka.magento.services.ProductMediaService;
import com.github.chen0040.eureka.magento.services.ProductService;
import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.Product;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Executors;


/**
 * Created by xschen on 18/9/2017.
 */
@Controller
public class ProductController {

   @Autowired
   private ProductService productService;

   @Autowired
   private ProductMediaService productMediaService;

   @Autowired
   private MagentoEventService eventService;

   @Autowired
   private CategoryService categoryService;

   private static final String CATEGORY = "product";

   private ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

   @RequestMapping(value="/magento/products/{sku}", method = RequestMethod.GET)
   public @ResponseBody ProductViewModel getProductBySku(@PathVariable(value = "sku") String sku) {
      Product p = productService.getProduct(sku);
      String username = productService.getUsernameBySku(sku);

      if(p == null){
         eventService.sendError(CATEGORY, "get product by sku failed", "No product found for sku " + sku);
         return null;
      }

      eventService.sendMessage(CATEGORY, "get product by sku", "Return product " + p.getName() + " with sku " + sku);

      ProductViewModel viewModel = new ProductViewModel(p, username);

      String url = productMediaService.getProductImageUrl(p);

      viewModel.setImageUrl(url);
      return viewModel;
   }

   @RequestMapping(value="/magento/products", method = RequestMethod.POST,  consumes="application/json")
   public @ResponseBody ProductViewModel saveProduct(@RequestBody ProductViewModel product) {
      final Product p = productService.saveProduct(product);

      if(p == null) {
         eventService.sendError(CATEGORY, "save product failed", "Failed to save the product into magento " + product.getSku());
         return new ProductViewModel("Failed to save product " + product.getSku() + ", you may have a duplicated product name");
      }

      productService.addProductToVendor(p, product.getVendor());

      executor.submit(() -> {
         boolean isNewProduct = StringUtils.isEmpty(product.getCreated_at());
         if(isNewProduct) {
            categoryService.addProductToCategory(product.getSku(), categoryService.getRootCategory().getId());
            categoryService.updateCategory(categoryService.getRootCategory().getId());
            categoryService.updateCategoryTree();
         }

      });

      return new ProductViewModel(p, product.getVendor());
   }

   @RequestMapping(value="/magento/products/{sku}", method = RequestMethod.DELETE)
   public @ResponseBody String deleteProduct(@PathVariable("sku")  String sku) {
      productService.removeProductFromVendor(sku);
      String result = productService.deleteProduct(sku);


      eventService.sendMessage(CATEGORY, "delete product by sku", "sku " + sku + " deleted: " + result);
      executor.submit(() -> {
         List<Category> categories = categoryService.findBySku(sku);
         for(Category c : categories) {
            categoryService.updateCategory(c.getId());
         }
         categoryService.updateCategoryTree();
      });

      return result;
   }

   @RequestMapping(value="/magento/new-product", method = RequestMethod.GET)
   public @ResponseBody ProductViewModel newProduct(@RequestParam("username") String username) {
      ProductViewModel product = new ProductViewModel(new Product(), username);;
      eventService.sendMessage(CATEGORY, "new product", "new product by vendor " + username + " with sku " + product.getSku());
      return product;
   }
}
