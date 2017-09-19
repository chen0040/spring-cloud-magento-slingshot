package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.magento.models.CategoryProduct;
import com.github.chen0040.magento.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by xschen on 18/9/2017.
 */
@Service
public class SearchServiceImpl implements SearchService {

   @Autowired
   private CategoryService categoryService;

   @Autowired
   private ProductService productService;

   @Override public List<CategoryProduct> search(String keyword) {
      String keyword2 = keyword.toLowerCase();
      List<CategoryProduct> all = categoryService.getProductsInCategory(categoryService.getRootCategory().getId());
      return all.stream().filter(cp -> {
         Product product = productService.getProduct(cp.getSku());
         return product.getName().toLowerCase().contains(keyword2);
      }).collect(Collectors.toList());
   }
}
