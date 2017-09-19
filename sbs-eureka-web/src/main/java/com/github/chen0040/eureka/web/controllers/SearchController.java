package com.github.chen0040.eureka.web.controllers;


import com.github.chen0040.eureka.web.api.SearchApi;
import com.github.chen0040.magento.models.CategoryProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by xschen on 18/9/2017.
 */
@RestController
public class SearchController {

   @Autowired
   private SearchApi searchApi;

   @RequestMapping(value="/magento/search", method = RequestMethod.GET)
   public @ResponseBody List<CategoryProduct> search(@RequestParam("keyword") String keyword) {
      return searchApi.search(keyword);
   }
}
