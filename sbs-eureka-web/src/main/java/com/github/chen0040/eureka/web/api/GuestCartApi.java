package com.github.chen0040.eureka.web.api;


import com.github.chen0040.commons.models.MagentoCart;
import com.github.chen0040.magento.models.CartItem;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * Created by xschen on 18/9/2017.
 */
@FeignClient("sbs-eureka-app-magento")
public interface GuestCartApi {
   @RequestMapping(value="/magento/cart/guest/new-cart", method = RequestMethod.GET)
   @ResponseBody String newCart();

   @RequestMapping(value="/magento/cart/guest/{cartId}", method = RequestMethod.GET)
   @ResponseBody MagentoCart getCart(@PathVariable("cartId") String cartId);

   @RequestMapping(value = "/magento/cart/guest/{cartId}/items", method = RequestMethod.POST, consumes = "application/json")
   @ResponseBody MagentoCart addItemToCart(@PathVariable("cartId") String cartId, @RequestBody CartItem item);

   @RequestMapping(value="/magento/cart/guest/{cartId}/items/{itemId}", method = RequestMethod.DELETE)
   @ResponseBody MagentoCart deleteItemInCart(@PathVariable("cartId") String cartId, @PathVariable("itemId") int itemId);

   @RequestMapping(value="/magento/cart/guest/{cartId}/items", method = RequestMethod.DELETE)
   @ResponseBody MagentoCart removeAllCartItems(@PathVariable("cartId") String cartId);
}
