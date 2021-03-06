package com.github.chen0040.eureka.magento.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


/**
 * Created by xschen on 23/6/2017.
 */
@Entity
@Table(name = "product_model")
@Getter
@Setter
public class Product3DModel {
   @Id
   private String sku;

   @Lob
   private byte[] model;
}
