package com.alr.products.service.message.producer;

import com.alr.products.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProductMessageProducer {

  private RabbitTemplate rabbitTemplate;
  private String exchange;
  private String routingkey;

  public ProductMessageProducer(
    @Autowired RabbitTemplate rabbitTemplate,
    @Value("${application.rabbitmq.exchange}") String exchange,
    @Value("${application.rabbitmq.routingkey}") String routingkey
  ) {
    this.rabbitTemplate = rabbitTemplate;
    this.exchange = exchange;
    this.routingkey = routingkey;
  }

  public void sendMessage(List<Product> products) {
    log.info(String.format("Product to process: %d", products.size()));
    products.forEach(product -> {
      sendMessage(product);
    });
  }

  private void sendMessage(Product product) {
    log.info(String.format("Product to process: %s", product.toString()));
    rabbitTemplate.convertAndSend(exchange, routingkey, product);
  }
}
