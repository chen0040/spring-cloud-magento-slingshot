package com.github.chen0040.eureka.magento.services;


import com.github.chen0040.eureka.magento.repositories.MagentoRepository;
import com.github.chen0040.eureka.magento.utils.HttpUtils;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.enums.ImageType;
import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;
import com.github.chen0040.magento.models.MagentoAttribute;
import com.github.chen0040.magento.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by xschen on 13/6/2017.
 */
@Service
public class MagentoServiceImpl implements MagentoService {
   private static final Logger logger = LoggerFactory.getLogger(MagentoServiceImpl.class);
   @Autowired
   private MagentoClient magentoClient;

   private Map<Long, Category> categories = new HashMap<>();

   private Category rootCategory = null;

   @Autowired
   private MagentoRepository repository;

   @Override
   public Category getRootCategory() {
      if(rootCategory == null) {
         rootCategory = magentoClient.categories().all();
         categories.clear();
         collect(rootCategory);
      }
      return rootCategory;
   }

   private void collect(Category x){
      categories.put(x.getId(), x);
      for(Category c : x.getChildren_data()){
         collect(c);
      }
   }

   @Override
   public List<Category> getTopCategories() {
      return getRootCategory().getChildren_data();
   }


   @Override public Category getCategoryById(long categoryId) {
      if(rootCategory == null) {
         getRootCategory();
      }
      return categories.get(categoryId);
   }

   @Override public long getParentCategoryId(long categoryId) {
      Category c = getCategoryById(categoryId);
      return c.getParent_id();
   }


   @Override public void downloadImage(String sku) {
      String url = getMagentoProductImageUrl(sku);

      byte[] bytes = null;

      try {
         bytes = HttpUtils.getBytes(url);
         InputStream in = new ByteArrayInputStream(bytes);
         BufferedImage img = ImageIO.read(in);

         img = resizeImage(img, 300, 300);
         String path = "/tmp/" + sku + ".img";
         ImageIO.write(img, "png", new File(path));
      }
      catch (Exception exception) {
         logger.error("Failed to get bytes from image " + url, exception);
      }


   }

   public static BufferedImage resizeImage(BufferedImage originalImage, int IMG_WIDTH, int IMG_HEIGHT){
      int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
      BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
      Graphics2D g = resizedImage.createGraphics();
      g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
      g.dispose();

      return resizedImage;
   }


   @Override public List<CategoryProduct> getProductsInCategory(long categoryId) {
      List<CategoryProduct> result = repository.getProductsInCategory(categoryId);
      if(result == null) {
         result = magentoClient.categories().getProductsInCategory(categoryId);
         repository.setProductsInCategory(categoryId, result);
      }
      return result;
   }

   @Override public List<CategoryProduct> search(String keyword) {
      String keyword2 = keyword.toLowerCase();
      List<CategoryProduct> all = getProductsInCategory(getRootCategory().getId());
      return all.stream().filter(cp -> {
         Product product = getProduct(cp.getSku());
         return product.getName().toLowerCase().contains(keyword2);
      }).collect(Collectors.toList());
   }

   @Override public Product getProduct(String sku) {
      Product result = repository.getProduct(sku);
      if(result == null) {
         result = magentoClient.products().getProductBySku(sku);
         repository.setProduct(sku, result);
      }
      return result;
   }

   @Override public Product saveProduct(Product product) {
      Product p = magentoClient.products().addProduct(product);
      repository.setProduct(product.getSku(), product);
      rootCategory = null;
      repository.clearProductsInCategory();

      logger.info("product saved: {}", p);
      return p;
   }

   @Override public String deleteProduct(String sku) {
      String result = magentoClient.products().deleteProduct(sku);
      rootCategory = null;
      repository.removeProduct(sku);
      repository.clearProductsInCategory();

      return result;
   }

   @Override public String getProductImageUrl(String sku) {
      String url = repository.getProductImageUrl(sku);
      if(url == null) {
         url = getMagentoProductImageUrl(sku);
         repository.setMagentoProductImageUrl(sku, url);
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

   @Override public String getProductImageUrl(Product p) {
      String sku = p.getSku();
      String url = repository.getProductImageUrl(sku);

      if(url != null) {
         return url;
      }

      String id = p.getCustom_attributes().stream().filter(attr -> attr.getAttribute_code().equals("image")).map(MagentoAttribute::getValue).findFirst().orElse("");



      return magentoClient.getBaseUri() + "/pub/media/catalog/product" + id;
   }


   @Override public long uploadProductImagePng(String sku, byte[] bytes) {
      long id = magentoClient.media().uploadImage(sku, bytes, ImageType.Png, true);
      repository.removeMagentoProductImageUrl(sku);
      repository.setProductImage(sku, bytes);
      return id;
   }

   @Override public long uploadProductImageJpeg(String sku, byte[] bytes) {
      long id = magentoClient.media().uploadImage(sku, bytes, ImageType.Jpeg, true);
      repository.removeMagentoProductImageUrl(sku);
      repository.setProductImage(sku, bytes);
      return id;
   }


   @Override public boolean addProductToCategory(String sku, long categoryId) {
      boolean added = magentoClient.categories().addProductToCategory(categoryId, sku, 1);

      rootCategory = null;
      repository.removeProductsInCategory(categoryId);

      return added;
   }


   @Override public byte[] getProductImage(String sku) {
      if(repository.hasProductImage(sku)){
         return repository.getProductImage(sku);
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
}
