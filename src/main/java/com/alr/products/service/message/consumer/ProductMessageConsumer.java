package com.alr.products.service.message.consumer;

import com.alr.products.entity.Product;
import com.alr.products.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ProductMessageConsumer {

  private ProductRepository productRepository;

  public ProductMessageConsumer(
    @Autowired ProductRepository productRepository
  ) {
    this.productRepository = productRepository;
  }

  @RabbitListener(queues = { "products-processor" })
  public void consumeMessage(Product productFromMessage) {
    Optional<Product> product = productRepository.findById(productFromMessage.getId());
    if (product.isEmpty()) {
      productRepository.save(productFromMessage);

      log.info(productFromMessage.toString() + " added to the database");
    }
  }
}
