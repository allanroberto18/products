package com.alr.products.helper;

import com.alr.products.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProductSerializerHelper {

  public String serialize(final List<Product> products) {
    String jsonGenerated = "";
    try {
      jsonGenerated = new ObjectMapper().writeValueAsString(products);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }

    return jsonGenerated;
  }

  public Product deserialize(final String json) {
    Product product = null;

    try {
      product = new ObjectMapper().readValue(json, Product.class);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }

    return product;
  }
}
