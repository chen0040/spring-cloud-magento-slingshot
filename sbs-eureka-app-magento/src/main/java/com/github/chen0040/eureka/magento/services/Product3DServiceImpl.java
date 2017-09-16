package com.github.chen0040.eureka.magento.services;

import com.github.chen0040.eureka.magento.models.Product3DModel;
import com.github.chen0040.eureka.magento.repositories.Product3DRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Created by xschen on 26/6/2017.
 */
@Service
public class Product3DServiceImpl implements Product3DService {
   @Autowired
   private Product3DRepository repository;

   @Override public Product3DModel findBySku(String sku) {
      return repository.findOne(sku);
   }

   @Transactional
   @Override public String save(Product3DModel model) {
      model = repository.save(model);
      return model.getProductSku();
   }
}
