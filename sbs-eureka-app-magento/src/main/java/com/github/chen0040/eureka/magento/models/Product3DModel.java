package com.github.chen0040.eureka.magento.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


/**
 * Created by xschen on 23/6/2017.
 */
@Entity
@Table(name = "product_models", indexes = {
        @Index(name="userIdIndex", columnList = "userId")
})
@Getter
@Setter
public class Product3DModel {

   private long userId;

   @Id
   private String productSku;

   @Lob
   private byte[] model;

   @Lob
   private byte[] model2;

}
