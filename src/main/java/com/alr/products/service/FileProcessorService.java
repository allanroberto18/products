package com.alr.products.service;

import com.alr.products.entity.Product;
import com.alr.products.entity.ProductFile;
import com.alr.products.helper.FileHelper;
import com.alr.products.helper.FileLoaderHelper;
import com.alr.products.repository.ProductFileRepository;
import com.alr.products.service.message.producer.ProductMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FileProcessorService {

  private FileHelper fileHelper;
  private FileLoaderHelper fileLoaderHelper;
  private ProductFileRepository productFileRepository;
  private ProductMessageProducer productMessageProducer;

  public FileProcessorService(
    @Autowired FileHelper fileHelper,
    @Autowired FileLoaderHelper fileLoaderHelper,
    @Autowired ProductFileRepository productFileRepository,
    @Autowired ProductMessageProducer productMessageProducer
  ) {
    this.fileHelper = fileHelper;
    this.fileLoaderHelper = fileLoaderHelper;
    this.productFileRepository = productFileRepository;
    this.productMessageProducer = productMessageProducer;
  }

  public void processFile(final MultipartFile file) throws IOException {
    if (saveFile(file)) {
      List<Product> products = this.fileLoaderHelper.loadContent(file.getInputStream());

      productMessageProducer.sendMessage(products);
    }
  }

  private Boolean saveFile(final MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    Optional<ProductFile> productFileOptional = productFileRepository.findByName(originalFilename);
    if (productFileOptional.isEmpty()) {
      saveProductFile(file);
      return true;
    }

    return false;
  }

  private void saveProductFile(MultipartFile file) {
    ProductFile productFile = ProductFile
      .builder()
      .name(file.getOriginalFilename())
      .build();

    fileHelper.saveFile(file);

    productFileRepository.save(productFile);
  }
}
