package com.github.chen0040.eureka.magento.repositories;


import com.github.chen0040.eureka.magento.models.UserProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by xschen on 18/9/2017.
 */
@Repository
public interface VendorProductRepository extends CrudRepository<UserProduct, String> {
}
