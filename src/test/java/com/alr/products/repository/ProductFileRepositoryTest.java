package com.alr.products.repository;

import com.alr.products.entity.ProductFile;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductFileRepositoryTest {

  @Autowired
  private ProductFileRepository productFileRepository;

  @Test
  @Order(1)
  public void findByName_WithInvalidNameAsParameter_MustReturnOptionalEmpty() {
    String name = "ProductFile 123";

    Optional<ProductFile> ProductFileExpected = productFileRepository.findByName(name);

    Assertions.assertTrue(ProductFileExpected.isEmpty());
  }

  @Test
  @Order(2)
  public void save_WithProductFileEntityAsParameter_MustReturnProductFileWithId() {
    String name = "ProductFile 123";

    ProductFile productFileExpected = saveProductFileToTest(name);

    productFileRepository
      .findById(productFileExpected.getId())
      .ifPresent(currentProductFile -> Assertions.assertEquals(currentProductFile.getId(), productFileExpected.getId()));

    Assertions.assertNotNull(productFileExpected.getId());
  }

  @Test
  @Order(3)
  public void findByName_WithValidNameAsParameter_MustReturnOptionalProductFile() {
    String name = "ProductFile 123";

    Optional<ProductFile> ProductFileExpected = productFileRepository.findByName(name);

    Assertions.assertTrue(ProductFileExpected.get().getName().equals(name));
  }

  private ProductFile createProductFile(String name) {
    return ProductFile
      .builder()
      .name(name)
      .build();
  }

  private ProductFile saveProductFileToTest(String name) {
    ProductFile ProductFile = createProductFile(name);

    return productFileRepository.save(ProductFile);
  }
}
