package com.alr.products.helper;

import com.alr.products.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FileLoaderHelper {

  public List<Product> loadContent(final InputStream inputStream) {
    Workbook workbook = getWorkBook(inputStream);
    Sheet sheet = workbook.getSheetAt(0);
    Integer categoryId = extractCategory(sheet);

    return mountProductList(sheet, categoryId);
  }

  private Workbook getWorkBook(final InputStream inputStream) {
    Workbook workbook = null;

    try {
      workbook = new XSSFWorkbook(inputStream);
    } catch (IOException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }

    return workbook;
  }

  private Integer extractCategory(final Sheet sheet) {
    Row categoryRow = sheet.getRow(0);
    Integer categoryId = (int) categoryRow.getCell(1).getNumericCellValue();

    log.info(String.format("Category Id: %d", categoryId));

    return categoryId;
  }

  private List<Product> mountProductList(Sheet sheet, Integer categoryId) {
    List<Product> products = new ArrayList<>();
    int index = 0;
    for (Row row : sheet) {
      if (index > 1) {
        Integer im = (int) row.getCell(0).getNumericCellValue();
        String name = row.getCell(1).getStringCellValue();
        Boolean freeShipping = ((int) row.getCell(2).getNumericCellValue() == 1);
        String description = row.getCell(3).getStringCellValue();
        Double price = Double.parseDouble(row.getCell(4).getStringCellValue());

        Product product = Product
          .builder()
          .id(im)
          .category(categoryId)
          .name(name)
          .freeShipping(freeShipping)
          .description(description)
          .price(price)
          .build();

        products.add(product);
      }
      index++;
    }

    return products;
  }
}
