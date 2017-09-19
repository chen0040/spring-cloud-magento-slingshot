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
public class MyCartController {

   private static final Logger logger = LoggerFactory.getLogger(MyCartController.class);

   @Value("${magento.site.url}")
   private String magentoSiteUrl;

   private MagentoClient client(String token) {
      MagentoClient client = new MagentoClient(magentoSiteUrl);
      client.setToken(token);
      return client;
   }

   @RequestMapping(value="/magento/cart/me/new-quote", method = RequestMethod.GET)
   public @ResponseBody String newQuote(@RequestParam("token") String token){
      logger.info("[/magento/cart/me/new-quote?token={}] invoked.", token);
      MagentoClient client = client(token);
      String quoteId = client.myCart().newQuote();
      logger.info("Obtain new quote id for user {}: {}", token, quoteId);
      return quoteId;
   }

   @RequestMapping(value="/magento/cart/me", method = RequestMethod.GET)
   private @ResponseBody MagentoCart getCart(@RequestParam("token") String token, @RequestParam("quoteId") String quoteId) {
      MagentoClient client = client(token);
      Cart cart = client.myCart().getCart();
      CartTotal cartTotal = client.myCart().getCartTotal();

      logger.info("cart: \r\n{}", JSON.toJSONString(cart, SerializerFeature.PrettyFormat));
      logger.info("cartTotal: \r\n{}", JSON.toJSONString(cartTotal, SerializerFeature.PrettyFormat));

      MagentoCart magentoCart = new MagentoCart(cart, cartTotal, quoteId);
      return magentoCart;
   }

   @RequestMapping(value = "/magento/cart/me/{quoteId}/items", method = RequestMethod.POST, consumes = "application/json")
   public @ResponseBody MagentoCart addItemToCart(
           @PathVariable("quoteId") String quoteId,
           @RequestParam("token") String token,
           @RequestBody CartItem item){

      MagentoClient client = client(token);
      Cart cart = client.myCart().getCart();

      CartItem existingItem = getExistingItem(cart, item.getSku());
      if(existingItem != null){
         existingItem.setQty(existingItem.getQty() + item.getQty());
         item = existingItem;
         item = client.myCart().updateItemInCart(quoteId, item);
      } else {
         item = client.myCart().addItemToCart(quoteId, item);
      }

      return getCart(token, quoteId);
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


   @RequestMapping(value="/magento/cart/me/{quoteId}/items/{itemId}", method = RequestMethod.DELETE)
   public @ResponseBody MagentoCart deleteItemInCart(@PathVariable("quoteId") String quoteId,
           @PathVariable("itemId") int itemId,
           @RequestParam("token") String token){
      MagentoClient client = client(token);
      boolean result = client.myCart().deleteItemInCart(itemId);
      logger.info("deleted: {}", result);
      return getCart(token, quoteId);
   }

   @RequestMapping(value="/magento/cart/me/{quoteId}/items", method = RequestMethod.DELETE)
   public @ResponseBody MagentoCart removeAllCartItems(@PathVariable("quoteId") String quoteId, @RequestParam("token") String token){
      MagentoClient client = client(token);
      Cart cart = client.myCart().getCart();
      if(cart != null) {
         List<CartItem> items = cart.getItems();
         for(int i=0; i < items.size(); ++i) {
            CartItem item = items.get(i);
            client.myCart().deleteItemInCart(item.getItem_id());
         }
      }

      return getCart(token, quoteId);
   }
}
