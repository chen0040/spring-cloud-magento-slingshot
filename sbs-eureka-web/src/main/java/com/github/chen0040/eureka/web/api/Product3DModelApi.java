package com.github.chen0040.eureka.web.api;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by xschen on 18/9/2017.
 */
@FeignClient("sbs-eureka-app-magento")
public interface Product3DModelApi {
   @RequestMapping(value = "/magento/products/{sku}/3d-model", method = RequestMethod.GET)
   @ResponseBody String getProduct3DModel(@PathVariable("sku") String sku);

   @RequestMapping(value = "/magento/products/{sku}/3d-model/exists", method = RequestMethod.GET)
   @ResponseBody boolean product3DModelExists(@PathVariable("sku") String sku);
}
