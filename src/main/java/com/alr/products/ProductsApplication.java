package com.alr.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ProductsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductsApplication.class, args);
  }
}
