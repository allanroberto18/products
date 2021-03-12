package com.alr.products.service;

import com.alr.products.entity.Product;
import com.alr.products.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceTest {

  @Autowired
  private ProductService productService;

  @MockBean
  private ProductRepository productRepository;

  @Test
  public void update_WithProductAsArgument_MustReturnProductUpdated() {
    Integer id = 1;

    Product productFromSearch = getProductForTestPurpose(id, "Product 1", "Description 1");
    Product productToUpdate = getProductForTestPurpose(id, "Product 2", "Description 2");

    when(productRepository.findById(id)).thenReturn(Optional.of(productFromSearch));
    when(productRepository.save(productToUpdate)).thenReturn(productToUpdate);

    Product productExpected = productService.updateProduct(id, productToUpdate);

    Assertions.assertEquals(productExpected.getName(), productToUpdate.getName());
    Assertions.assertEquals(productExpected.getDescription(), productToUpdate.getDescription());
  }

  @Test
  public void update_WithProductThatDoesntExistAsArgument_MustNull() {
    Integer id = 1;

    Product productToUpdate = getProductForTestPurpose(id, "Product 2", "Description 2");

    when(productRepository.findById(id)).thenReturn(Optional.empty());
    when(productRepository.save(productToUpdate)).thenReturn(productToUpdate);

    Product productExpected = productService.updateProduct(id, productToUpdate);

    Assertions.assertNull(productExpected);
  }

  @Test
  public void findProductById_WithIdAsParameter_MustReturnProduct() {
    int id = 1;

    Product productFromSearch = getProductForTestPurpose(id, "Product 1", "Description 1");

    when(productRepository.findById(id)).thenReturn(Optional.of(productFromSearch));

    Optional<Product> productExpected = productService.findProductById(id);

    Assertions.assertNotNull(productExpected);
  }

  @Test
  public void findProductById_WithIdAsParameter_MustReturnOptionalEmpty() {
    int id = 1;

    when(productRepository.findById(id)).thenReturn(Optional.empty());

    Optional<Product> productExpected = productService.findProductById(id);

    Assertions.assertTrue(productExpected.isEmpty());
  }

  @Test
  public void deleteProduct_WithIdAsParameterButProductNotFound_ShouldThrowException() {
    int id = 1;

    when(productRepository.findById(id)).thenReturn(Optional.empty());

    assertThatExceptionOfType(Exception.class).isThrownBy(() -> productService.deleteProduct(id));
  }

  @Test
  public void deleteProduct_WithIdAsParameter_ShouldNotThrowException() {
    int id = 1;

    Product productFromSearch = getProductForTestPurpose(id, "Product 1", "Description 1");

    when(productRepository.findById(id)).thenReturn(Optional.of(productFromSearch));

    assertThatCode(() -> productService.deleteProduct(id)).doesNotThrowAnyException();
  }

  @Test
  public void getAll_WithNoArgument_MustReturnListOfProducts() {
    List<Product> products = List.of(
      getProductForTestPurpose(1, "Product 1", "Description 1"),
      getProductForTestPurpose(2, "Product 2", "Description 2")
    );

    when(productRepository.findAll()).thenReturn(products);

    List<Product> productsExpected = productService.findAll();

    Assertions.assertTrue(productsExpected.size() == 2);
  }

  private Product getProductForTestPurpose(Integer id, String s, String s2) {
    return Product
      .builder()
      .id(id)
      .category(123)
      .name(s)
      .description(s2)
      .freeShipping(false)
      .price(10D)
      .build();
  }
}
