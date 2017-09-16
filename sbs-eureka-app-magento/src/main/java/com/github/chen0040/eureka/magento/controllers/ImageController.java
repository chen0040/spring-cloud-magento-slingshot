package com.github.chen0040.eureka.magento.controllers;


import com.github.chen0040.commons.utils.StringUtils;
import com.github.chen0040.eureka.magento.services.MagentoService;
import com.github.chen0040.eureka.magento.utils.ResourceFileUtils;
import com.github.chen0040.magento.models.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by xschen on 27/6/2017.
 */
@Controller
public class ImageController {

   private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

   @Autowired private MagentoService service;


   @RequestMapping(value = "/product-img/{sku}", method = RequestMethod.GET)
   public void getProductImage(@PathVariable("sku") String sku, HttpServletResponse response, HttpServletRequest request)
           throws ServletException, IOException {

      byte[] bytes = service.getProductImage(sku);

      if (bytes != null) {
         response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
         response.getOutputStream().write(bytes);

         response.getOutputStream().close();
      }
   }


   @RequestMapping(value = "/magento/categories/{categoryId}/icon", method = RequestMethod.GET)
   public void getCategoryIcon(@PathVariable("categoryId") long categoryId, HttpServletResponse response, HttpServletRequest request)
           throws ServletException, IOException {
      logger.info("get category icon for category {}", categoryId);
      String filename = getFilenameByCategoryId(categoryId);

      while(!ResourceFileUtils.resourceExists(filename)){
         categoryId = service.getParentCategoryId(categoryId);
         filename = getFilenameByCategoryId(categoryId);
         if(StringUtils.isEmpty(filename)) break;
      }

      if(StringUtils.isEmpty(filename)) return;

      byte[] bytes = ResourceFileUtils.getBytes(filename);

      if (bytes != null) {
         response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
         response.getOutputStream().write(bytes);

         response.getOutputStream().close();
      }
   }

   private String getFilenameByCategoryId(long categoryId) {
      Category category = service.getCategoryById(categoryId);
      if(category ==null) return "";
      String filename = "static/img/category_" + category.getName().toLowerCase().replace(' ', '_') + ".png";

      System.out.println(filename);

      if(filename.contains("cainet")) {
         filename = filename.replace("cainet", "cabinet");
      }
      if(filename.contains("bean_bag")) {
         filename = filename.replace("bean_bag", "bean_bags");
      }
      return filename;
   }

}
