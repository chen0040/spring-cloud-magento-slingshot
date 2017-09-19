package com.github.chen0040.eureka.web.controllers;


import com.github.chen0040.commons.utils.StringUtils;
import com.github.chen0040.eureka.web.api.Product3DModelApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;


/**
 * Created by xschen on 18/9/2017.
 */
@Controller
public class Product3DModelController {

   @Autowired
   private Product3DModelApi product3DModelApi;

   @RequestMapping(value = "/magento/products/{sku}/3d-model", method = RequestMethod.GET)
   public void getProduct3DModel(@PathVariable("sku") String sku,
           HttpServletResponse response,
           HttpServletRequest request)
           throws ServletException, IOException {

      String modelBase64 = product3DModelApi.getProduct3DModel(sku);

      if(!StringUtils.isEmpty(modelBase64)) {
         byte[] bytes = Base64.getDecoder().decode(modelBase64);
         response.setContentType("text/plain");
         response.getOutputStream().write(bytes);

         response.getOutputStream().close();
      }
   }

   @RequestMapping(value = "/magento/products/{sku}/3d-model/exists", method = RequestMethod.GET)
   public @ResponseBody boolean product3DModelExists(@PathVariable("sku") String sku)
           throws ServletException, IOException {

      return product3DModelApi.product3DModelExists(sku);
   }
}
