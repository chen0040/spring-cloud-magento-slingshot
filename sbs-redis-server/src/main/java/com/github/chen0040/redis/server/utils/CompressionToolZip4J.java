package com.github.chen0040.redis.server.utils;


import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;


/**
 * Created by xschen on 16/4/2017.
 */
public class CompressionToolZip4J implements CompressionTool {
   private static Logger logger = LoggerFactory.getLogger(CompressionToolZip4J.class);

   @Override
   public void unzip(String src, String dest, String password) {
      try {
         ZipFile zipFile = new ZipFile(src);
         if (zipFile.isEncrypted()) {
            zipFile.setPassword(password);
         }
         zipFile.extractAll(dest);
      }
      catch (ZipException e) {
         logger.error("Failed to unzip " + src + " to " + dest, e);
      }

      logger.info("File decompressed: {}", src);
   }

   @Override
   public void zip(String src, String dest, String password) {
      zipIt(src, dest, password, false);
   }


   public void zipIt(String src, String dest, String password, boolean weak) {
      if (weak) {
         zip4jItWeak(src, dest, password);
      }
      else {
         zip4jIt(src, dest, password);
      }
   }


   public void zip4jIt(String src, String dest, String password) {
      try {
         //This is name and path of zip file to be created
         ZipFile zipFile = new ZipFile(dest);

         //Add files to be archived into zip file
         ArrayList<File> filesToAdd = new ArrayList<>();
         filesToAdd.add(new File(src));

         //Initiate Zip Parameters which define various properties
         ZipParameters parameters = new ZipParameters();
         parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression

         //DEFLATE_LEVEL_FASTEST     - Lowest compression level but higher speed of compression
         //DEFLATE_LEVEL_FAST        - Low compression level but higher speed of compression
         //DEFLATE_LEVEL_NORMAL  - Optimal balance between compression level/speed
         //DEFLATE_LEVEL_MAXIMUM     - High compression level with a compromise of speed
         //DEFLATE_LEVEL_ULTRA       - Highest compression level but low speed
         parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

         if (password != null) {
            //Set the encryption flag to true
            parameters.setEncryptFiles(true);

            //Set the encryption method to AES Zip Encryption
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);

            //AES_STRENGTH_128 - For both encryption and decryption
            //AES_STRENGTH_192 - For decryption only
            //AES_STRENGTH_256 - For both encryption and decryption
            //Key strength 192 cannot be used for encryption. But if a zip file already has a
            //file encrypted with key strength of 192, then Zip4j can decrypt this file
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

            //Set password
            parameters.setPassword(password);
         }

         //Now add files to the zip file
         zipFile.addFiles(filesToAdd, parameters);
      }
      catch (ZipException e) {
         logger.error("Failed to zip: " + src, e);
      }
   }


   public void zip4jItWeak(String src, String dest, String password) {
      try {
         //This is name and path of zip file to be created
         ZipFile zipFile = new ZipFile(dest);

         //Add files to be archived into zip file
         ArrayList<File> filesToAdd = new ArrayList<>();
         filesToAdd.add(new File(src));

         //Initiate Zip Parameters which define various properties
         ZipParameters parameters = new ZipParameters();
         parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression

         //DEFLATE_LEVEL_FASTEST     - Lowest compression level but higher speed of compression
         //DEFLATE_LEVEL_FAST        - Low compression level but higher speed of compression
         //DEFLATE_LEVEL_NORMAL  - Optimal balance between compression level/speed
         //DEFLATE_LEVEL_MAXIMUM     - High compression level with a compromise of speed
         //DEFLATE_LEVEL_ULTRA       - Highest compression level but low speed
         parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

         if (password != null) {
            //Set the encryption flag to true
            parameters.setEncryptFiles(true);

            //Set the encryption method to AES Zip Encryption
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);

            //AES_STRENGTH_128 - For both encryption and decryption
            //AES_STRENGTH_192 - For decryption only
            //AES_STRENGTH_256 - For both encryption and decryption
            //Key strength 192 cannot be used for encryption. But if a zip file already has a
            //file encrypted with key strength of 192, then Zip4j can decrypt this file
            //parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

            //Set password
            parameters.setPassword(password);
         }

         //Now add files to the zip file
         zipFile.addFiles(filesToAdd, parameters);
      }
      catch (ZipException e) {
         logger.error("Failed to zip: " + src, e);
      }
   }


   public static void gzipIt(String src, String dest) {

      byte[] buffer = new byte[1024];

      FileInputStream in = null;
      GZIPOutputStream gzos = null;
      try {

         gzos = new GZIPOutputStream(new FileOutputStream(dest));

         in = new FileInputStream(src);

         int len;
         while ((len = in.read(buffer)) > 0) {
            gzos.write(buffer, 0, len);
         }

         logger.info("Gzip completed: " + dest);

      }
      catch (IOException ex) {
         logger.error("Gzip failed: " + src, ex);
      } finally {
         if (in != null) {
            try {
               in.close();
            }
            catch (IOException e) {
               logger.error("Exception encountered", e);
            }
         }

         if (gzos != null) {
            try {
               gzos.finish();
            }
            catch (IOException e) {
               logger.error("Exception encountered", e);
            }
            try {
               gzos.close();
            }
            catch (IOException e) {
               logger.error("Exception encountered", e);
            }
         }
      }
   }
}
