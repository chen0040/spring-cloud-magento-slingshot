package com.github.chen0040.eureka.magento.repositories;

import com.github.chen0040.eureka.magento.models.Product3DModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by xschen on 26/6/2017.
 */
@Repository
public interface Product3DRepository extends CrudRepository<Product3DModel, String> {

}
