package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.commons.viewmodels.ProductViewModel;
import com.github.chen0040.magento.models.Product;


/**
 * Created by xschen on 13/6/2017.
 */
public interface ProductService {
   Product getProduct(String sku);
   Product saveProduct(Product product);
   String deleteProduct(String sku);
   String getUsernameBySku(String sku);

   void addProductToVendor(ProductViewModel product, String username);
}
