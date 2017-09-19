package com.github.chen0040.eureka.magento.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;


/**
 * Created by xschen on 18/9/2017.
 */
@Entity
@Table(name = "user_product", indexes = {
        @Index(name="usernameIndex", columnList = "username")
})
@Getter
@Setter
public class UserProduct {
   private String username;
   @Id
   private String sku;
}
