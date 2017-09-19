package com.github.chen0040.eureka.web.api;


import com.github.chen0040.magento.models.CategoryProduct;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * Created by xschen on 18/9/2017.
 */
@FeignClient("sbs-eureka-app-magento")
public interface SearchApi {
   @RequestMapping(value="/magento/search", method = RequestMethod.GET)
   @ResponseBody List<CategoryProduct> search(@RequestParam("keyword") String keyword);
}
