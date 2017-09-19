package com.github.chen0040.eureka.web.controllers;


import com.github.chen0040.commons.viewmodels.ShippingOptionViewModel;
import com.github.chen0040.eureka.web.api.LogisticsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by xschen on 18/9/2017.
 */
@Controller
public class LogisticsController {

   @Autowired
   private LogisticsApi logisticsApi;

   @RequestMapping(value="/magento/logistics/shipping-options", method = RequestMethod.GET)
   @ResponseBody List<ShippingOptionViewModel> getShipmentOptions4Product(@RequestParam("sku") String sku) {
      return logisticsApi.getShipmentOptions4Product(sku);
   }

   @RequestMapping(value="/magento/logistics/shipping-options/{id}", method = RequestMethod.GET)
   public @ResponseBody ShippingOptionViewModel getShippingOptionById(@PathVariable("id") long id) {
      return logisticsApi.getShippingOptionById(id);
   }
}
