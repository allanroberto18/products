package com.alr.products.controller;

import com.alr.products.entity.Product;
import com.alr.products.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/product")
@Api(value ="Products", tags = "Update, List, Select and Remove products from the Database")
public class ProductController {

  private ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @ApiOperation(value = "Get all the products", response = List.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully retrieved list"),
  })
  @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> getProducts() {
    List<Product> products = productService.findAll();

    return new ResponseEntity(products, HttpStatus.OK);
  }

  @ApiOperation(value = "Get a product considering the id", response = Product.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully retrieved product"),
    @ApiResponse(code = 400, message = "Bad argument, check the values and try again"),
    @ApiResponse(code = 404, message = "Product not found")
  })
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> getProductById(final @Valid @PathVariable(name = "id") Integer id) {
    Optional<Product> optProduct = productService.findProductById(id);
    if (optProduct.isEmpty()) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity(optProduct.get(), HttpStatus.OK);
  }

  @ApiOperation(value = "Delete a product considering the id")
  @ApiResponses(value = {
    @ApiResponse(code = 202, message = "Successfully deleted the product"),
    @ApiResponse(code = 400, message = "Bad argument, check the values and try again"),
    @ApiResponse(code = 404, message = "Product not found")
  })
  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> deleteProductById(final @Valid @PathVariable(name = "id") Integer id) {
    try {
      productService.deleteProduct(id);
    } catch (Exception e) {
      e.printStackTrace();

      log.info(String.format("Product id: %d not found", id));
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity(null, HttpStatus.ACCEPTED);
  }

  @ApiOperation(value = "Update a product considering the id", response = Product.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully updated the product"),
    @ApiResponse(code = 400, message = "Bad argument, check the values and try again"),
    @ApiResponse(code = 404, message = "Product not found")
  })
  @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> updateProductById(final @Valid @PathVariable(name = "id") Integer id, final @Valid @RequestBody Product product) {
    Product productUpdated = productService.updateProduct(id, product);
    if (Optional.ofNullable(productUpdated).isEmpty()) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity(productUpdated, HttpStatus.OK);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }
}
