package com.github.chen0040.eureka.magento.controllers;


import com.github.chen0040.commons.utils.StringUtils;
import com.github.chen0040.eureka.magento.services.CategoryService;
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
 * Created by xschen on 18/9/2017.
 */
@Controller
public class CategoryMediaController {

   private static final Logger logger = LoggerFactory.getLogger(CategoryMediaController.class);

   @Autowired
   private CategoryService categoryService;

   @RequestMapping(value = "/magento/categories/{categoryId}/icon", method = RequestMethod.GET)
   public void getCategoryIcon(@PathVariable("categoryId") long categoryId, HttpServletResponse response, HttpServletRequest request)
           throws ServletException, IOException {
      logger.info("get category icon for category {}", categoryId);
      String filename = getFilenameByCategoryId(categoryId);

      while(!ResourceFileUtils.resourceExists(filename)){
         categoryId = categoryService.getParentCategoryId(categoryId);
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
      Category category = categoryService.getCategoryById(categoryId);
      if(category ==null) return "";
      String filename = "static/img/category_" + category.getName().toLowerCase().replace(' ', '_') + ".png";

      System.out.println(filename);
      return filename;
   }
}
