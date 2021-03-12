package com.alr.products.repository;

import com.alr.products.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  @Test
  @Order(1)
  public void findByName_WithInvalidNameAsArgument_MustReturnOptionalEmpty() {
    String name = "Product 123";

    Optional<Product> productExpected = productRepository.findByName(name);

    Assertions.assertTrue(productExpected.isEmpty());
  }

  @Test
  @Order(2)
  public void save_ProductAsArgument_MustReturnProduct() {
    Integer id = 123;
    Integer category = 123456;
    String name = "Product 123";
    String description = "Some Description";
    Boolean freeShipping = true;
    Double price = 10.00;

    Product product = buildProduct(category, id, name, description, freeShipping, price);

    Product productExpected = productRepository.save(product);

    productRepository
      .findById(id)
      .ifPresent(currentProduct -> Assertions.assertEquals(currentProduct.getName(), productExpected.getName()));

    Assertions.assertNotNull(productExpected.getId());
    Assertions.assertTrue(!id.equals(0));
  }

  @Test
  @Order(3)
  public void findByName_WithValidNameAsArgument_MustReturnOptional() {
    String name = "Product 123";

    Optional<Product> productExpected = productRepository.findByName(name);

    Assertions.assertTrue(productExpected.get().getName().equals(name));
  }

  @Test
  @Order(4)
  public void findProductByCategory_WithCategoryAsArgument_MustReturnListOfProducts() {
    Integer category = 123456;

    List<Product> products = productRepository.findAllByCategory(category);

    Assertions.assertTrue(products.size() > 0);
  }

  @Test
  @Order(5)
  public void save_WithNewNameAsArgument_MustReturnProduct() {
    String currentName = "Product 123";

    Optional<Product> currentProduct = productRepository.findByName(currentName);

    String newName = "Product 234";

    currentProduct.ifPresent(product -> {
      product.setName(newName);
      productRepository.save(product);
    });

    Optional<Product> productExpected = productRepository.findByName(newName);

    Assertions.assertTrue(productExpected.get().getName().equals(newName));
  }

  @Test
  @Order(6)
  public void delete_WithCategory_MustDeleteProduct() {
    String name = "Product 234";

    Optional<Product> currentProduct = productRepository.findByName(name);

    currentProduct.ifPresent(productRepository::delete);

    Optional<Product> productExpected = productRepository.findByName(name);

    Assertions.assertTrue(productExpected.isEmpty());
  }

  private Product buildProduct(
    Integer category,
    Integer id,
    String name,
    String description,
    Boolean freeShipping,
    Double price
  ) {
    return Product
      .builder()
      .id(id)
      .name(name)
      .description(description)
      .freeShipping(freeShipping)
      .price(price)
      .category(category)
      .build();
  }
}
