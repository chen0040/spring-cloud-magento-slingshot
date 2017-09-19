package com.github.chen0040.eureka.magento.controllers;


import com.github.chen0040.eureka.magento.services.SearchService;
import com.github.chen0040.magento.models.CategoryProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * Created by xschen on 18/9/2017.
 */
@Controller
public class SearchController {

   @Autowired
   private SearchService service;

   @RequestMapping(value="/magento/search", method = RequestMethod.GET)
   public @ResponseBody List<CategoryProduct> search(@RequestParam("keyword") String keyword) {
      return service.search(keyword);
   }
}
