package com.github.chen0040.commons.viewmodels;


import com.github.chen0040.magento.models.Product;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by xschen on 24/6/2017.
 */
@Getter
@Setter
public class ProductViewModel extends Product {
   private String imageUrl;
   private String vendor;
   private String error = "";

   public ProductViewModel(Product p, String vendor){
      this.setAttribute_set_id(p.getAttribute_set_id());
      this.setCreated_at(p.getCreated_at());
      this.setCustom_attributes(p.getCustom_attributes());
      this.setExtension_attributes(p.getExtension_attributes());
      this.setId(p.getId());
      this.setName(p.getName());
      this.setPrice(p.getPrice());
      this.setSku(p.getSku());
      this.setProduct_links(p.getProduct_links());
      this.setStatus(p.getStatus());
      this.setTier_prices(p.getTier_prices());
      this.setType_id(p.getType_id());
      this.setUpdated_at(p.getUpdated_at());
      this.setWeight(p.getWeight());
      this.setVisibility(p.getVisibility());
      this.vendor = vendor;
   }

   public ProductViewModel() {

   }

   public ProductViewModel(String error) {
      this.error = error;
   }
}
