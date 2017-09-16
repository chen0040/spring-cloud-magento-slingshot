package com.github.chen0040.eureka.magento.controllers;

import com.github.chen0040.eureka.magento.models.Product3DModel;
import com.github.chen0040.eureka.magento.services.Product3DService;
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
import java.util.HashMap;
import java.util.Map;


/**
 * Created by xschen on 26/6/2017.
 */
@Controller
public class Product3DController {

   private static final Logger logger = LoggerFactory.getLogger(Product3DController.class);

   @Autowired private Product3DService service;

   @RequestMapping(value = "/product-3d/{sku}", method = RequestMethod.GET)
   public void getProduct3DModel(@PathVariable("sku") String sku,
           HttpServletResponse response,
           HttpServletRequest request)
           throws ServletException, IOException {

      Product3DModel model = service.findBySku(sku);

      if(model != null) {
         byte[] bytes = model.getModel();
         response.setContentType("text/plain");
         response.getOutputStream().write(bytes);

         response.getOutputStream().close();
      }
   }

   @RequestMapping(value = "/product-3d/{sku}/exists", method = RequestMethod.GET)
   public @ResponseBody boolean product3DModelExists(@PathVariable("sku") String sku)
           throws ServletException, IOException {

      Product3DModel model = service.findBySku(sku);

      if(model != null) {
         byte[] bytes = model.getModel();
         return bytes != null;
      }
      return false;
   }

   @RequestMapping(value = "/users/{userId}/product-3d-model-2/{sku}", method = RequestMethod.GET)
   public void getProduct3DModel2(
           @PathVariable("sku") String sku,
           @PathVariable("userId") long userId,
           HttpServletResponse response,
           HttpServletRequest request)
           throws ServletException, IOException {

      Product3DModel model = service.findBySku(sku);

      if(model != null) {
         byte[] bytes = model.getModel2();
         response.setContentType("text/plain");
         response.getOutputStream().write(bytes);

         response.getOutputStream().close();
      }
   }

   @RequestMapping(value = "/users/{userId}/product-3d-model-2/{sku}", method = RequestMethod.POST)
   public @ResponseBody Map<String, Object> uploadProduct3DModel2(
           @PathVariable("sku") String sku,
           @PathVariable("userId") long userId,
           @RequestParam("file") MultipartFile file,
           HttpServletRequest request)
           throws ServletException, IOException {

      Product3DModel model = service.findBySku(sku);

      if(model == null) {
         model = new Product3DModel();
         model.setProductSku(sku);
         model.setUserId(userId);
      }

      try {
         byte[] bytes = file.getBytes();
         model.setModel2(bytes);
         logger.info("icon bytes received: {}", bytes.length);

         String id = service.save(model);

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


   @RequestMapping(value = "/users/{userId}/product-3d/{sku}", method = RequestMethod.POST)
   public @ResponseBody Map<String, Object> uploadProduct3DModel(
           @PathVariable("userId") long userId,
           @PathVariable("sku") String sku,
           @RequestParam("file") MultipartFile file,
           HttpServletRequest request)
           throws ServletException, IOException {

      Product3DModel model = service.findBySku(sku);

      if(model == null) {
         model = new Product3DModel();
         model.setProductSku(sku);
         model.setUserId(userId);
      }

      try {
         byte[] bytes = file.getBytes();
         model.setModel(bytes);
         logger.info("icon bytes received: {}", bytes.length);

         String id = service.save(model);

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
}
