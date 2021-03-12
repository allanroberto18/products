package com.alr.products.configuration;

import com.alr.products.service.message.consumer.ProductMessageConsumer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

  @Value("${application.rabbitmq.exchange}")
  private String exchange;

  @Value("${application.rabbitmq.queueName}")
  private String queueName;

  @Value("${application.rabbitmq.routingkey}")
  private String routingKey;

  @Bean
  public Queue queue() {
    return new Queue(queueName, false);
  }

  @Bean
  public DirectExchange topicExchange() {
    return new DirectExchange(exchange);
  }

  @Bean
  public Binding binding(Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(routingKey);
  }

  @Bean
  public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  public MessageListenerAdapter listenerAdapter(ProductMessageConsumer productMessageConsumer) {
    return new MessageListenerAdapter(productMessageConsumer, "consumeMessage");
  }
}
