package com.github.chen0040.eureka.web.controllers;


import com.github.chen0040.eureka.web.api.ProductMediaApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by xschen on 18/9/2017.
 */
@Controller
public class ProductMediaController {

   private static final Logger logger = LoggerFactory.getLogger(ProductMediaController.class);

   @Autowired
   private ProductMediaApi productMediaApi;

   @RequestMapping(value="/magento/upload/product-image-{imageType}/{sku}", method = RequestMethod.POST)
   public @ResponseBody Map<String, Object> uploadCompanyIcon(
           @PathVariable("sku") String sku,
           @PathVariable("imageType") String imageType,
           @RequestParam("file") MultipartFile file,
           HttpServletRequest request) {

      logger.info("POST[/magento/upload/product-image-{}/{}] invoked.", imageType, sku);

      try {
         byte[] bytes = file.getBytes();
         logger.info("icon bytes received: {}", bytes.length);

         String imgBase64 = Base64.getEncoder().encodeToString(bytes);

         productMediaApi.saveProductImage(sku, imageType, imgBase64);

         Map<String, Object> result = new HashMap<>();
         result.put("success", true);
         result.put("id", sku);

         return result;
      }catch(IOException ex) {
         logger.error("Failed to process the uploaded icon", ex);
         Map<String, Object> result = new HashMap<>();
         result.put("success", false);
         result.put("reason", ex.getMessage());
         return result;
      }
   }

   @RequestMapping(value = "/magento/products/{sku}/image", method = RequestMethod.GET)
   public void getProductImage(@PathVariable("sku") String sku, HttpServletResponse response, HttpServletRequest request)
           throws ServletException, IOException {

      String imgBase64 = productMediaApi.getProductImage(sku);

      byte[] bytes = null;
      try{
         bytes = Base64.getDecoder().decode(imgBase64);
      }catch(Exception exception) {
         logger.error("Failed to decode image", exception);
      }

      if (bytes != null) {
         response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
         response.getOutputStream().write(bytes);

         response.getOutputStream().close();
      }
   }
}
