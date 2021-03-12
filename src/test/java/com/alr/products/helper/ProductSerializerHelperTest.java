package com.alr.products.helper;

import com.alr.products.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductSerializerHelperTest {

  @Autowired
  private ProductSerializerHelper productSerializerHelper;

  @Test
  public void serialize_WithListOfProductsAsParameter_MustReturnString() {
    List<Product> products = List.of(
      Product
        .builder()
          .id(1)
          .name("Product 1")
          .category(123)
          .description("Description 1")
          .freeShipping(false)
          .price(10D)
        .build(),
      Product
        .builder()
        .id(2)
        .name("Product 2")
        .category(123)
        .description("Description 2")
        .freeShipping(false)
        .price(20D)
        .build()
    );

    String jsonGenerated = productSerializerHelper.serialize(products);

    Assertions.assertNotNull(jsonGenerated);
    Assertions.assertTrue(jsonGenerated.contains("\"name\":\"Product 1\""));
    Assertions.assertTrue(jsonGenerated.contains("\"name\":\"Product 2\""));
  }

  @Test
  public void deserialize_WithStringJsonAsArgument_MustReturnListOfProducts() {
    String json = getJsonForTest();

    Product productGenerated = productSerializerHelper.deserialize(json);

    Assertions.assertTrue(productGenerated instanceof Product);
    Assertions.assertTrue(productGenerated.getName().equals("Product 1"));
  }

  private String getJsonForTest() {
    return "{\"id\":1,\"category\":123,\"name\":\"Product 1\",\"freeShipping\":false,\"description\":\"Description 1\",\"price\":10.0}";
  }
}
