package com.github.chen0040.commons.models;


import com.github.chen0040.magento.models.Cart;
import com.github.chen0040.magento.models.CartItem;
import com.github.chen0040.magento.models.CartTotal;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by xschen on 18/9/2017.
 */
@Getter
@Setter
public class MagentoCart {
   private Cart cart;
   private CartTotal cartTotal;
   private String id;
   private Map<String, Long> shippingOptions = new HashMap<>();
   private int status = 0;

   public MagentoCart() {

   }

   public MagentoCart(String id) {
      this.id = id;
   }

   public MagentoCart(Cart cart, CartTotal cartTotal, String id) {
      this.cart = cart;
      this.cartTotal = cartTotal;
      this.id = id;
   }


   public void setShipping(String sku, long shippingId) {
      for(CartItem item : cart.getItems()){
         if(item.getSku().equals(sku)){
            shippingOptions.put(sku, shippingId);
            break;
         }
      }
   }
}
