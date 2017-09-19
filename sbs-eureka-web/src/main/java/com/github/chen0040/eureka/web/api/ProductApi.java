package com.github.chen0040.eureka.web.api;


import com.github.chen0040.commons.viewmodels.ProductViewModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * Created by xschen on 18/9/2017.
 */
@FeignClient("sbs-eureka-app-magento")
public interface ProductApi {
   @RequestMapping(value="/magento/products/{sku}", method = RequestMethod.GET)
   @ResponseBody ProductViewModel getProductBySku(@PathVariable(value = "sku") String sku);

   @RequestMapping(value="/magento/products", method = RequestMethod.POST,  consumes="application/json")
   @ResponseBody ProductViewModel saveProduct(@RequestBody ProductViewModel product);

   @RequestMapping(value="/magento/products/{sku}", method = RequestMethod.DELETE)
   @ResponseBody String deleteProduct(@PathVariable("sku") String sku);

   @RequestMapping(value="/magento/new-product", method = RequestMethod.GET)
   @ResponseBody ProductViewModel newProduct(@RequestParam("username") String username);
}
