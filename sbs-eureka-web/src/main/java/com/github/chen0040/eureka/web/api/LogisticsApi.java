package com.github.chen0040.eureka.web.api;


import com.github.chen0040.commons.viewmodels.ShippingOptionViewModel;
import com.github.chen0040.commons.viewmodels.ShippingOrderViewModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by xschen on 18/9/2017.
 */
@FeignClient("sbs-eureka-app-logistics")
public interface LogisticsApi {
   @RequestMapping(value="logistics/shipping-options", method = RequestMethod.GET)
   @ResponseBody List<ShippingOptionViewModel> getShipmentOptions4Product(@RequestParam("sku") String sku);

   @RequestMapping(value="logistics/shipping-options/{id}", method = RequestMethod.GET)
   @ResponseBody ShippingOptionViewModel getShippingOptionById(@PathVariable("id") long id);

   @RequestMapping(value="logistics/shipping-order", method= RequestMethod.GET)
   @ResponseBody ShippingOrderViewModel createShippingOrder(@RequestParam("shippingId") long shippingId, @RequestParam("sku") String sku,
           @RequestParam("qty") int qty);
}
