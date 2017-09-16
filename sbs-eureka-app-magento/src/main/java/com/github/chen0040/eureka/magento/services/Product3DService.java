package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.eureka.magento.models.Product3DModel;


/**
 * Created by xschen on 26/6/2017.
 */
public interface Product3DService {
   Product3DModel findBySku(String sku);
   String save(Product3DModel model);
}
