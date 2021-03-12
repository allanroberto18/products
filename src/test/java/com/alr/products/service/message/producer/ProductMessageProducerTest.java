package com.alr.products.service.message.producer;

import com.alr.products.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductMessageProducerTest {

  @MockBean
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private ProductMessageProducer productMessageProducer;

  @Test
  public void sendMessage_WithListOfProductsAsArgument() {
    List<Product> products = List.of(
      Product
        .builder()
          .id(1)
          .category(123)
          .name("Product 1")
          .description("Description 1")
          .freeShipping(false)
          .price(10D)
        .build(),
      Product
        .builder()
        .id(2)
        .category(123)
        .name("Product 2")
        .description("Description 2")
        .freeShipping(false)
        .price(20D)
        .build()
    );

    assertThatCode(() -> productMessageProducer.sendMessage(products)).doesNotThrowAnyException();
  }
}
