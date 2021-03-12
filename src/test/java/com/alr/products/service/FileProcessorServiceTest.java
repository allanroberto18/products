package com.alr.products.service;

import com.alr.products.entity.Product;
import com.alr.products.entity.ProductFile;
import com.alr.products.helper.FileHelper;
import com.alr.products.helper.FileLoaderHelper;
import com.alr.products.repository.ProductFileRepository;
import com.alr.products.service.message.producer.ProductMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
public class FileProcessorServiceTest {

  @MockBean
  private FileHelper fileHelper;

  @MockBean
  private ProductFileRepository productFileRepository;

  @MockBean
  private FileLoaderHelper fileLoaderHelper;

  @MockBean
  private ProductMessageProducer productMessageProducer;

  @Autowired
  private FileProcessorService fileProcessorService;

  private static String PATH_TEST = "src/test/resources/files";
  private static String FILE_NAME = "products_teste.xlsx";

  @Test
  public void processFile_WithFileAsParameter_WhenProductFileNotFoundOnDB_ShouldNotThrowException() throws IOException {
    String filePathExpected = String.format("%s/%s", PATH_TEST, FILE_NAME);
    MockMultipartFile multipartFile = getMockMultipartFile();

    List<Product> products = List.of(
      getProductForTestPurpose(1, "Product 1", "Description 1"),
      getProductForTestPurpose(2, "Product 2", "Description 2")
    );

    when(fileHelper.saveFile(multipartFile)).thenReturn(filePathExpected);
    when(productFileRepository.findByName(filePathExpected)).thenReturn(Optional.empty());
    when(fileLoaderHelper.loadContent(multipartFile.getInputStream())).thenReturn(products);

    assertThatCode(() -> fileProcessorService.processFile(multipartFile)).doesNotThrowAnyException();
  }

  @Test
  public void processFile_WithFileAsParameter_WhenProductFileWasFound_MustReturnListOfProducts() throws IOException {
    String filePathExpected = String.format("%s/%s", PATH_TEST, FILE_NAME);
    Optional<ProductFile> optProductFile = Optional.of(
      ProductFile
        .builder()
        .name(FILE_NAME)
        .build()
    );

    List<Product> products = List.of(
      getProductForTestPurpose(1, "Product 1", "Description 1"),
      getProductForTestPurpose(2, "Product 2", "Description 2")
    );

    MockMultipartFile multipartFile = getMockMultipartFile();
    when(fileHelper.saveFile(multipartFile)).thenReturn(filePathExpected);
    when(productFileRepository.findByName(FILE_NAME)).thenReturn(optProductFile);
    when(fileLoaderHelper.loadContent(multipartFile.getInputStream())).thenReturn(products);

    assertThatCode(() -> fileProcessorService.processFile(multipartFile)).doesNotThrowAnyException();
  }

  private MockMultipartFile getMockMultipartFile() {
    return new MockMultipartFile("file", FILE_NAME,
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sh",
      FILE_NAME.getBytes()
    );
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
