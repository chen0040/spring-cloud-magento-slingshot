package com.github.chen0040.eureka.magento.components;


import com.google.common.util.concurrent.*;
import com.github.chen0040.eureka.magento.services.CategoryService;
import com.github.chen0040.eureka.magento.services.ProductMediaService;
import com.github.chen0040.eureka.magento.services.ProductService;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;


/**
 * Created by xschen on 15/12/2016.
 */
@Component
public class MagentoSyncScheduler {

   private static final Logger logger = LoggerFactory.getLogger(MagentoSyncScheduler.class);


   private ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

   private static final long duration = 10000L;

   @Value("${magento.admin.username}")
   private String magentoUsername;

   @Value("${magento.admin.password}")
   private String magentoPassword;

   @Autowired
   private MagentoClient magentoClient;

   @Autowired
   private ProductService productService;

   @Autowired
   private ProductMediaService productMediaService;

   @Autowired
   private CategoryService categoryService;

   private boolean contentSyncEnabled = false;

   @Scheduled(fixedDelay = 1800000L) //every 30 minutes
   public void acquireMagentoToken() {
      ListenableFuture<String> future = service.submit(()-> magentoClient.loginAsAdmin(magentoUsername, magentoPassword));

      Futures.addCallback(future, new FutureCallback<String>() {
         @Override public void onSuccess(String glock) {
            logger.info("magento token updated: {}", glock);
         }

         @Override public void onFailure(Throwable throwable) {
            logger.error("Something goes wrong with the magento login", throwable);
         }
      });
   }

   @Scheduled(fixedDelay = 86400000L) // every 1 day
   public void syncAllCategories(){
      if(!contentSyncEnabled) return;

      ListenableFuture<Long> future = service.submit(() -> {
         long startTime = System.currentTimeMillis();
         Category rootCategory = categoryService.getRootCategory();
         syncCategory(rootCategory);
         return System.currentTimeMillis() - startTime;
      });
      Futures.addCallback(future, new FutureCallback<Long>() {
         @Override public void onSuccess(Long duration) {
            logger.info("categories synchronization completed: {} milliseconds", duration);
         }


         @Override public void onFailure(Throwable throwable) {
            logger.error("Failed to sync categories", throwable);
         }
      });

   }

   @Scheduled(fixedDelay = 86400000L) // every 1 day
   public void syncAllProductImages(){
      if(!contentSyncEnabled) return;

      ListenableFuture<Long> future = service.submit(() -> {
         long startTime = System.currentTimeMillis();
         Category rootCategory = categoryService.getRootCategory();
         syncProductImage(rootCategory);
         return System.currentTimeMillis() - startTime;
      });
      Futures.addCallback(future, new FutureCallback<Long>() {
         @Override public void onSuccess(Long duration) {
            logger.info("product images synchronization completed: {} milliseconds", duration);
         }


         @Override public void onFailure(Throwable throwable) {
            logger.error("Failed to sync product images", throwable);
         }
      });

   }

   private void syncCategory(Category category) {
      long categoryId = category.getId();
      logger.info("refreshing category {}", categoryId);

      List<Category> children = category.getChildren_data();

      for(Category child : children) {
         syncCategory(child);
      }

      List<CategoryProduct> categoryProductList = categoryService.getProductsInCategory(categoryId);
      for(CategoryProduct cp : categoryProductList){
         productService.getProduct(cp.getSku());
      }

      logger.info("category refreshed for {}", categoryId);
   }

   private void syncProductImage(Category category) {
      List<CategoryProduct> categoryProductList = categoryService.getProductsInCategory(category.getId());
      for(CategoryProduct product : categoryProductList) {
         logger.info("downloading image for sku {}", product.getSku());
         productMediaService.downloadImage(product.getSku());
      }
   }



}
