package com.github.chen0040.eureka.web.api;


import com.github.chen0040.commons.models.MagentoCart;
import com.github.chen0040.magento.models.CartItem;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * Created by xschen on 18/9/2017.
 */
@FeignClient("sbs-eureka-app-magento")
public interface MyCartApi {

   @RequestMapping(value="/magento/cart/me/new-quote", method = RequestMethod.GET)
   @ResponseBody String newQuote(@RequestParam("token") String token);

   @RequestMapping(value="/magento/cart/me", method = RequestMethod.GET)
   @ResponseBody MagentoCart getCart(@RequestParam("token") String token, @RequestParam("quoteId") String quoteId);

   @RequestMapping(value = "/magento/cart/me/{quoteId}/items", method = RequestMethod.POST, consumes = "application/json")
   @ResponseBody MagentoCart addItemToCart(@PathVariable("quoteId") String quoteId, @RequestParam("token") String token, @RequestBody CartItem item);

   @RequestMapping(value="/magento/cart/me/{quoteId}/items/{itemId}", method = RequestMethod.DELETE)
   @ResponseBody MagentoCart deleteItemInCart(@PathVariable("quoteId") String quoteId, @PathVariable("itemId") int itemId,
           @RequestParam("token") String token);

   @RequestMapping(value="/magento/cart/me/{quoteId}/items", method = RequestMethod.DELETE)
   @ResponseBody MagentoCart removeAllCartItems(@PathVariable("quoteId") String quoteId, @RequestParam("token") String token);
}
