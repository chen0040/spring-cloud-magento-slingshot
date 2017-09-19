package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.magento.models.Product;


/**
 * Created by xschen on 18/9/2017.
 */
public interface ProductMediaService {
   String getProductImageUrl(String sku);
   String getProductImageUrl(Product p);

   long uploadProductImagePng(String sku, byte[] bytes);
   long uploadProductImageJpeg(String sku, byte[] bytes);

   byte[] getProductImage(String sku);

   void downloadImage(String sku);
}
