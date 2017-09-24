package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.commons.viewmodels.ProductViewModel;
import com.github.chen0040.eureka.magento.models.UserProduct;
import com.github.chen0040.eureka.magento.repositories.ProductCache;
import com.github.chen0040.eureka.magento.repositories.VendorProductRepository;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Created by xschen on 13/6/2017.
 */
@Service
public class ProductServiceImpl implements ProductService {
   private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

   @Autowired
   private MagentoClient magentoClient;

   @Autowired
   private ProductCache productCache;

   @Autowired
   private VendorProductRepository vendorProductRepository;

   @Autowired
   private CategoryService categoryService;

   @Override public String getUsernameBySku(String sku) {
      UserProduct userProduct = vendorProductRepository.findOne(sku);
      if(userProduct == null) return "";
      return userProduct.getUsername();
   }

   @Transactional
   @Override public void addProductToVendor(Product product, String username) {
      UserProduct userProduct = new UserProduct();
      userProduct.setUsername(username);
      userProduct.setSku(product.getSku());

      if(vendorProductRepository.exists(product.getSku())){
         return;
      }
      vendorProductRepository.save(userProduct);
   }

   @Transactional
   @Override public void removeProductFromVendor(String sku) {
      vendorProductRepository.delete(sku);
   }


   @Override public Product getProduct(String sku) {
      Product result = productCache.getProduct(sku);
      if(result == null) {
         result = magentoClient.products().getProductBySku(sku);
         productCache.setProduct(sku, result);
      }
      return result;
   }

   @Override public Product saveProduct(Product product) {
      Product p = magentoClient.products().saveProduct(product);
      productCache.setProduct(product.getSku(), product);

      logger.info("product saved: {}", p);
      return p;
   }

   @Override public String deleteProduct(String sku) {
      String result = magentoClient.products().deleteProduct(sku);
      productCache.removeProduct(sku);

      return result;
   }





}
