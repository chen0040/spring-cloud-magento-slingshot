package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.eureka.magento.repositories.ProductMediaRepository;
import com.github.chen0040.eureka.magento.utils.HttpUtils;
import com.github.chen0040.eureka.magento.utils.ImageUtils;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.enums.ImageType;
import com.github.chen0040.magento.models.MagentoAttribute;
import com.github.chen0040.magento.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * Created by xschen on 18/9/2017.
 */
@Service
public class ProductMediaServiceImpl implements ProductMediaService {

   private static Logger logger = LoggerFactory.getLogger(ProductMediaServiceImpl.class);

   @Autowired
   private MagentoClient magentoClient;

   @Autowired
   private ProductMediaRepository productMediaRepository;

   @Override public byte[] getProductImage(String sku) {
      if(productMediaRepository.hasProductImage(sku)){
         return productMediaRepository.getProductImage(sku);
      } else {
         String url = getProductImageUrl(sku);

         byte[] bytes = null;

         try {
            bytes = HttpUtils.getBytes(url);
         }
         catch (IOException exception) {
            logger.error("Failed to get bytes from image " + url, exception);
         }
         return bytes;
      }
   }


   @Override public long uploadProductImagePng(String sku, byte[] bytes) {
      long id = magentoClient.media().uploadImage(sku, bytes, ImageType.Png, true);
      productMediaRepository.removeMagentoProductImageUrl(sku);
      productMediaRepository.setProductImage(sku, bytes);
      return id;
   }

   @Override public long uploadProductImageJpeg(String sku, byte[] bytes) {
      long id = magentoClient.media().uploadImage(sku, bytes, ImageType.Jpeg, true);
      productMediaRepository.removeMagentoProductImageUrl(sku);
      productMediaRepository.setProductImage(sku, bytes);
      return id;
   }


   @Override public String getProductImageUrl(Product p) {
      String sku = p.getSku();
      String url = productMediaRepository.getProductImageUrl(sku);

      if(url != null) {
         return url;
      }

      String id = p.getCustom_attributes().stream().filter(attr -> attr.getAttribute_code().equals("image")).map(MagentoAttribute::getValue).findFirst().orElse("");



      return magentoClient.getBaseUri() + "/pub/media/catalog/product" + id;
   }

   @Override public String getProductImageUrl(String sku) {
      String url = productMediaRepository.getProductImageUrl(sku);
      if(url == null) {
         url = getMagentoProductImageUrl(sku);
         productMediaRepository.setMagentoProductImageUrl(sku, url);
      }
      return url;
   }

   private String getMagentoProductImageUrl(String sku) {
      String url;
      List<String> urls = magentoClient.media().getProductMediaAbsoluteUrls(sku);
      if (urls.isEmpty())
         url = magentoClient.getBaseUri();
      else
         url = urls.get(0);
      return url;
   }

   @Override public void downloadImage(String sku) {
      String url = getMagentoProductImageUrl(sku);

      byte[] bytes = null;

      try {
         bytes = HttpUtils.getBytes(url);
         InputStream in = new ByteArrayInputStream(bytes);
         BufferedImage img = ImageIO.read(in);

         img = ImageUtils.resizeImage(img, 300, 300);
         String path = "/tmp/" + sku + ".img";
         ImageIO.write(img, "png", new File(path));
      }
      catch (Exception exception) {
         logger.error("Failed to get bytes from image " + url, exception);
      }


   }
}
