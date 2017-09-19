package com.github.chen0040.eureka.magento.utils;


import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Created by xschen on 18/9/2017.
 */
public class ImageUtils {
   public static BufferedImage resizeImage(BufferedImage originalImage, int IMG_WIDTH, int IMG_HEIGHT){
      int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
      BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
      Graphics2D g = resizedImage.createGraphics();
      g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
      g.dispose();

      return resizedImage;
   }
}
