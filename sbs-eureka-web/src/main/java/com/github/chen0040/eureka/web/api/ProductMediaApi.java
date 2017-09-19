package com.github.chen0040.eureka.web.api;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * Created by xschen on 18/9/2017.
 */
@FeignClient("sbs-eureka-app-magento")
public interface ProductMediaApi {

   @RequestMapping(value="/magento/upload/product-image-{imageType}/{sku}", method = RequestMethod.POST)
   @ResponseBody Map<String, Object> saveProductImage(@PathVariable("sku") String sku, @PathVariable("imageType") String imageType,
           @RequestBody String data);

   @RequestMapping(value = "/magento/products/{sku}/image", method = RequestMethod.GET)
   @ResponseBody String getProductImage(@PathVariable("sku") String sku);

}
