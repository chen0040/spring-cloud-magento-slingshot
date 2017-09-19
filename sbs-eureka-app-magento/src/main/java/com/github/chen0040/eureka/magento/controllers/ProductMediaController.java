package com.github.chen0040.eureka.magento.controllers;


import com.google.common.util.concurrent.*;
import com.github.chen0040.eureka.magento.components.SpringRequestHelper;
import com.github.chen0040.eureka.magento.services.ProductMediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;


/**
 * Created by xschen on 13/6/2017.
 */
@Controller
public class ProductMediaController {

   private static final Logger logger = LoggerFactory.getLogger(ProductMediaController.class);

   @Autowired
   private ProductMediaService productMediaService;

   @Autowired
   private SpringRequestHelper requestHelper;

   private ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

   @RequestMapping(value="/magento/upload/product-image-{imageType}/{sku}", method = RequestMethod.POST)
   public @ResponseBody Map<String, Object> saveProductImage(
           @PathVariable("sku") String sku,
           @PathVariable("imageType") String imageType,
           @RequestBody String data) {

      logger.info("POST[/magento/upload/product-image-{}/{}] invoked.", imageType, sku);

      Map<String, Object> result = new HashMap<>();
      result.put("success", true);
      result.put("id", sku);

      ListenableFuture<Long> future = executor.submit(()-> {

         byte[] bytes = Base64.getDecoder().decode(data);
         logger.info("icon bytes received: {}", bytes.length);

         long id;
         if (imageType.equals("png")) {
            id = productMediaService.uploadProductImagePng(sku, bytes);
         }
         else {
            id = productMediaService.uploadProductImageJpeg(sku, bytes);
         }

         return id;
      });

      Futures.addCallback(future, new FutureCallback<Long>() {
         @Override public void onSuccess(Long id) {
            logger.info("image successfully uploaded: {}", id);
         }


         @Override public void onFailure(Throwable throwable) {
            logger.error("Failed to process the uploaded icon", throwable);
         }
      });

      return result;
   }

   @RequestMapping(value = "/magento/products/{sku}/image", method = RequestMethod.GET)
   public @ResponseBody String getProductImage(@PathVariable("sku") String sku)
           throws IOException {
      byte[] bytes = productMediaService.getProductImage(sku);
      return Base64.getEncoder().encodeToString(bytes);
   }




}
