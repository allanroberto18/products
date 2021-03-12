package com.alr.products.service;

import com.alr.products.entity.Product;
import com.alr.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductService {

  private ProductRepository productRepository;

  public ProductService(
    @Autowired ProductRepository productRepository
  ) {
    this.productRepository = productRepository;
  }

  public Product updateProduct(final Integer id, final Product product) {
    Optional<Product> optProduct = productRepository.findById(id);

    if (optProduct.isEmpty()) {
      return null;
    }

    optProduct.ifPresent(currentProduct -> {
      Product productToUpdate  = Product
        .builder()
        .id(product.getId())
        .category(product.getCategory())
        .name(product.getName())
        .description(product.getDescription())
        .freeShipping(product.getFreeShipping())
        .price(product.getPrice())
        .build();

      productRepository.save(productToUpdate);
    });

    return product;
  }

  public Optional<Product> findProductById(final Integer id) {
    return productRepository.findById(id);
  }

  public void deleteProduct(final Integer id) throws Exception {
    Product product = productRepository.findById(id).orElseThrow(Exception::new);
    productRepository.delete(product);
  }

  public List<Product> findAll() {
    return productRepository.findAll();
  }
}
