package com.github.chen0040.eureka.magento.components;

import com.github.chen0040.eureka.magento.services.MagentoService;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;
import com.google.common.util.concurrent.*;
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
public class Scheduler {

   private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);


   private ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

   private static final long duration = 10000L;

   @Value("${magento.admin.username}")
   private String magentoUsername;

   @Value("${magento.admin.password}")
   private String magentoPassword;

   @Autowired
   private MagentoClient magentoClient;

   @Autowired
   private MagentoService magentoService;

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
   public void refreshCache(){
      ListenableFuture<Long> future = service.submit(() -> {
         long startTime = System.currentTimeMillis();
         Category rootCategory = magentoService.getRootCategory();
         refreshCategory(rootCategory);
         return System.currentTimeMillis() - startTime;
      });
      Futures.addCallback(future, new FutureCallback<Long>() {
         @Override public void onSuccess(Long duration) {
            logger.info("cache refresh completed: {} milliseconds", duration);
         }


         @Override public void onFailure(Throwable throwable) {
            logger.error("Failed to refresh cache", throwable);
         }
      });

   }

   @Scheduled(fixedDelay = 86400000L) // every 1 day
   public void refreshCachedImage(){
      ListenableFuture<Long> future = service.submit(() -> {
         long startTime = System.currentTimeMillis();
         Category rootCategory = magentoService.getRootCategory();
         refreshProductImage(rootCategory);
         return System.currentTimeMillis() - startTime;
      });
      Futures.addCallback(future, new FutureCallback<Long>() {
         @Override public void onSuccess(Long duration) {
            logger.info("cache refresh completed: {} milliseconds", duration);
         }


         @Override public void onFailure(Throwable throwable) {
            logger.error("Failed to refresh cache", throwable);
         }
      });

   }

   private void refreshCategory(Category category) {
      long categoryId = category.getId();
      logger.info("refreshing category {}", categoryId);

      List<Category> children = category.getChildren_data();

      for(Category child : children) {
         refreshCategory(child);
      }

      List<CategoryProduct> categoryProductList = magentoService.getProductsInCategory(categoryId);
      for(CategoryProduct cp : categoryProductList){
         magentoService.getProduct(cp.getSku());
      }

      logger.info("category refreshed for {}", categoryId);
   }

   private void refreshProductImage(Category category) {
      List<CategoryProduct> categoryProductList = magentoService.getProductsInCategory(category.getId());
      for(CategoryProduct product : categoryProductList) {
         logger.info("downloading image for sku {}", product.getSku());
         magentoService.downloadImage(product.getSku());
      }
   }



}
