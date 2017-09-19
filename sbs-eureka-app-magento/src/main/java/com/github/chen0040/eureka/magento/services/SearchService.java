package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.magento.models.CategoryProduct;

import java.util.List;


/**
 * Created by xschen on 18/9/2017.
 */
public interface SearchService {
   List<CategoryProduct> search(String keyword);
}
