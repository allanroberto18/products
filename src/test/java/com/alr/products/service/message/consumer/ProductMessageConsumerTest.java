package com.alr.products.service.message.consumer;

import com.alr.products.entity.Product;
import com.alr.products.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductMessageConsumerTest {

  @Autowired
  private ProductMessageConsumer productMessageConsumer;

  @MockBean
  private ProductRepository productRepository;

  @Test
  public void consumeMessage_WithProductThatExistOnDatabase_ShouldNotSaveOnDatabase() {
    Product product = buildProductToTest();

    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

    assertThatCode(() -> productMessageConsumer.consumeMessage(product)).doesNotThrowAnyException();
  }

  @Test
  public void consumeMessage_WithProductThatDoesntExistOnDatabase_ShouldNotSaveOnDatabase() {
    Product product = buildProductToTest();

    when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

    assertThatCode(() -> productMessageConsumer.consumeMessage(product)).doesNotThrowAnyException();
  }

  private Product buildProductToTest() {
    return Product
      .builder()
        .id(1)
        .category(123)
        .name("Product 1")
        .description("Description 1")
        .freeShipping(false)
        .price(10D)
      .build();
  }
}
