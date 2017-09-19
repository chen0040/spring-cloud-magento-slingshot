package com.github.chen0040.eureka.web.controllers;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.commons.models.MagentoCart;
import com.github.chen0040.commons.utils.StringUtils;
import com.github.chen0040.commons.utils.TupleThree;
import com.github.chen0040.eureka.web.api.GuestCartApi;
import com.github.chen0040.eureka.web.api.MyCartApi;
import com.github.chen0040.eureka.web.components.MagentoSession;
import com.github.chen0040.eureka.web.components.SpringAuthentication;
import com.github.chen0040.magento.models.CartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by xschen on 18/9/2017.
 */
@Controller
public class CartController {

   private static final Logger logger = LoggerFactory.getLogger(CartController.class);

   @Autowired
   private MyCartApi myCartApi;

   @Autowired
   private GuestCartApi guestCartApi;

   @Autowired
   private MagentoSession magentoSession;

   @Autowired
   private SpringAuthentication authentication;

   @Autowired
   private SimpMessagingTemplate brokerMessagingTemplate;

   @RequestMapping(value="/magento/cart", method = RequestMethod.GET)
   public @ResponseBody MagentoCart getCart(HttpServletRequest request) {

      logger.info("GET[/magento/cart] invoked.");

      TupleThree<String, Boolean, String> context = getCartContext(request);
      String token = context._1();
      boolean isAuthenticated = context._2();
      String cartId = context._3();

      MagentoCart result;
      if(isAuthenticated) {
         result = myCartApi.getCart(token, cartId);
      } else {
         result = guestCartApi.getCart(cartId);
      }

      magentoSession.storeCart(request, result);

      return result;
   }

   private TupleThree<String, Boolean, String> getCartContext(HttpServletRequest request) {
      String cartId = magentoSession.getCartId(request);
      final boolean isAuthenticated = authentication.isAuthenticated();
      final String token = authentication.getToken();
      if(StringUtils.isEmpty(cartId)) {
         if(isAuthenticated) {
            String quoteId = myCartApi.newQuote(token);
            magentoSession.storeCart(request, new MagentoCart(quoteId));
         } else {
            cartId = guestCartApi.newCart();
            magentoSession.storeCart(request, new MagentoCart(cartId));
         }
      }

      logger.info("context.token: {}", token);
      logger.info("context.authenticated: {}", isAuthenticated);
      logger.info("context.cart: {}", cartId);

      return new TupleThree<>(token, isAuthenticated, cartId);
   }

   @RequestMapping(value = "/magento/cart/add-item", method = RequestMethod.GET)
   @ResponseBody MagentoCart addItemToCart(@RequestParam("sku") String sku,
           @RequestParam("qty") int qty,
           @RequestParam("shippingId") long shippingId,
           HttpServletRequest request) {
      TupleThree<String, Boolean, String> context = getCartContext(request);
      String token = context._1();
      boolean isAuthenticated = context._2();
      String cartId = context._3();

      CartItem item = new CartItem();
      item.setQty(qty);
      item.setSku(sku);



      MagentoCart result;
      if(isAuthenticated) {
         result = myCartApi.addItemToCart(cartId, token, item);
      } else {
         result = guestCartApi.addItemToCart(cartId, item);
      }

      result.setShipping(sku, shippingId);
      magentoSession.storeCart(request, result);
      sendMessage(result, request);

      return result;
   }

   private void sendMessage(MagentoCart result, HttpServletRequest request) {
      String username = magentoSession.getUsername(request);
      brokerMessagingTemplate.convertAndSend("/topics/" + username + "/cart", JSON.toJSONString(result, SerializerFeature.BrowserCompatible));
   }

   @RequestMapping(value="/magento/cart/items/{itemId}", method = RequestMethod.DELETE)
   @ResponseBody MagentoCart deleteItemInCart(@PathVariable("itemId") int itemId, HttpServletRequest request) {
      TupleThree<String, Boolean, String> context = getCartContext(request);
      String token = context._1();
      boolean isAuthenticated = context._2();
      String cartId = context._3();

      MagentoCart result;
      if(isAuthenticated) {
         result = myCartApi.deleteItemInCart(cartId, itemId, token);
      } else {
         result = guestCartApi.deleteItemInCart(cartId, itemId);
      }

      magentoSession.storeCart(request, result);

      sendMessage(result, request);
      return result;
   }

   @RequestMapping(value="/magento/cart/items", method = RequestMethod.DELETE)
   @ResponseBody MagentoCart removeAllCartItems(HttpServletRequest request) {
      TupleThree<String, Boolean, String> context = getCartContext(request);
      String token = context._1();
      boolean isAuthenticated = context._2();
      String cartId = context._3();

      MagentoCart result;
      if(isAuthenticated) {
         result = myCartApi.removeAllCartItems(token, cartId);
      } else {
         result = guestCartApi.removeAllCartItems(cartId);
      }

      magentoSession.storeCart(request, result);

      return result;
   }
}
