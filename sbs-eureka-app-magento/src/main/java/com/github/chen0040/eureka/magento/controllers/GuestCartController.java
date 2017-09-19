package com.github.chen0040.eureka.magento.controllers;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.commons.models.MagentoCart;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.Cart;
import com.github.chen0040.magento.models.CartItem;
import com.github.chen0040.magento.models.CartTotal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by xschen on 18/9/2017.
 */
@Controller
public class GuestCartController {

   @Value("${magento.site.url}")
   private String magentoSiteUrl;

   private static final Logger logger = LoggerFactory.getLogger(GuestCartController.class);

   private MagentoClient client() {
      return new MagentoClient(magentoSiteUrl);
   }

   @RequestMapping(value="/magento/cart/guest/new-cart", method = RequestMethod.GET)
   public @ResponseBody String newCart(){
      String cartId = client().guestCart().newCart();
      return cartId;
   }

   @RequestMapping(value="/magento/cart/guest/{cartId}", method = RequestMethod.GET)
   public @ResponseBody MagentoCart getCart(@PathVariable("cartId") String cartId) {
      Cart cart = client().guestCart().getCart(cartId);
      CartTotal cartTotal = client().getGuestCart().getCartTotal(cartId);

      logger.info("cart: \r\n{}", JSON.toJSONString(cart, SerializerFeature.PrettyFormat));
      logger.info("cartTotal: \r\n{}", JSON.toJSONString(cartTotal, SerializerFeature.PrettyFormat));

      MagentoCart magentoCart = new MagentoCart(cart, cartTotal, cartId);
      return magentoCart;
   }

   @RequestMapping(value = "/magento/cart/guest/{cartId}/items", method = RequestMethod.POST, consumes = "application/json")
   public @ResponseBody MagentoCart addItemToCart(@PathVariable("cartId") String cartId, @RequestBody CartItem item){

      Cart cart = client().guestCart().getCart(cartId);

      CartItem existingItem = getExistingItem(cart, item.getSku());
      if(existingItem != null){
         existingItem.setQty(existingItem.getQty() + item.getQty());
         item = existingItem;
         item = client().guestCart().updateItemInCart(cartId, item);
      } else {
         item = client().guestCart().addItemToCart(cartId, item);
      }

      return getCart(cartId);
   }

   public CartItem getExistingItem(Cart cart, String sku) {
      if(cart != null) {
         if(cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
               if(item.getSku().equals(sku)){
                  return item;
               }
            }
         }
         return null;
      } else {
         return null;
      }
   }


   @RequestMapping(value="/magento/cart/guest/{cartId}/items/{itemId}", method = RequestMethod.DELETE)
   public @ResponseBody MagentoCart deleteItemInCart(@PathVariable("cartId") String cartId, @PathVariable("itemId") int itemId){
      boolean result = client().guestCart().deleteItemInCart(cartId, itemId);
      logger.info("deleted: {}", result);

      return getCart(cartId);
   }

   @RequestMapping(value="/magento/cart/guest/{cartId}/items", method = RequestMethod.DELETE)
   public @ResponseBody MagentoCart removeAllCartItems(@PathVariable("cartId") String cartId){
      MagentoClient client = client();
      Cart cart = client.guestCart().getCart(cartId);
      if(cart != null) {
         List<CartItem> items = cart.getItems();
         for(int i=0; i < items.size(); ++i) {
            CartItem item = items.get(i);
            client.guestCart().deleteItemInCart(cartId, item.getItem_id());
         }
      }
      return getCart(cartId);
   }


}
