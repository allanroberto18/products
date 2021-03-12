package com.alr.products.controller;

import com.alr.products.entity.Product;
import com.alr.products.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ProductController productController;

  @MockBean
  private ProductService productService;

  @Test
  public void contextLoads() throws Exception {
    Assertions.assertNotNull(productController);
  }

  @Test
  public void getProducts_WithNoArgument_MustReturnListOfProduct() throws Exception {
    List<Product> productsExpected = List.of(
      getProductForTestPurpose(1, "Product 1", "Description 1"),
      getProductForTestPurpose(2, "Product 2", "Description 2")
    );

    when(productService.findAll()).thenReturn(productsExpected);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/product/all")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .characterEncoding("UTF-8");

    this.mockMvc.perform(builder)
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(content().string(containsString("Product 1")));
  }

  @Test
  public void getProductById_WithIdAsArgument_MustReturnProduct() throws Exception {
    Integer id = 1;

    Product productExpected = getProductForTestPurpose(id, "Product 1", "Description 1");

    when(productService.findProductById(id)).thenReturn(Optional.of(productExpected));

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/product/{id}", id)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .characterEncoding("UTF-8");

    this.mockMvc.perform(builder)
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(content().string(containsString("Product 1")));
  }

  @Test
  public void getProductById_WithInvalidIdAsArgument_Must404Status() throws Exception {
    Integer id = 1;

    when(productService.findProductById(id)).thenReturn(Optional.empty());

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/product/{id}", id)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .characterEncoding("UTF-8");

    this.mockMvc.perform(builder)
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  public void getProductById_WithWrongTypeAsArgument_Must400Status() throws Exception {
    Integer id = 1;

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/product/{id}", "asdads")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .characterEncoding("UTF-8");

    this.mockMvc.perform(builder)
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  public void getProductById_WithoutArgument_Must400Status() throws Exception {
    Integer id = 1;

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/product/{id}", "")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .characterEncoding("UTF-8");

    this.mockMvc.perform(builder)
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  public void deleteProductById_IdAsArgument_Accepted() throws Exception {
    Integer id = 1;

    doNothing().when(productService).deleteProduct(id);

    this.mockMvc.perform(delete("/product/{id}", id))
      .andDo(print())
      .andExpect(status().isAccepted());
  }

  @Test
  public void deleteProductById_WithInvalidIdAsArgument_Must404Status() throws Exception {
    Integer id = 1;

    doThrow(new Exception()).when(productService).deleteProduct(id);

    this.mockMvc.perform(delete("/product/{id}", id))
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  public void deleteProductById_WithoutArgument_Must404Status() throws Exception {
    Integer id = 1;

    doThrow(new Exception()).when(productService).deleteProduct(id);

    this.mockMvc.perform(delete("/product/{id}", ""))
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  public void deleteProductById_WithInvalidTypeArgument_Must400Status() throws Exception {
    Integer id = 1;

    doThrow(new Exception()).when(productService).deleteProduct(id);

    this.mockMvc.perform(delete("/product/{id}", "asdas"))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  public void updateProductById_WithIdAndProductAsArgument_MustProductUpdated() throws Exception {
    Integer id = 1;

    Product productToUpdate = getProductForTestPurpose(id, "Product 1", "Product Description 1");
    when(productService.updateProduct(id, productToUpdate)).thenReturn(productToUpdate);
    String json = jsonForTestPurpose(productToUpdate);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/product/{id}", id)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .accept(MediaType.APPLICATION_JSON)
      .characterEncoding("UTF-8")
      .content(json);

    this.mockMvc.perform(builder).andExpect(status().isOk());
  }

  @Test
  public void updateProductById_WithInvalidIdAndProductAsArgument_Must404Status() throws Exception {
    Integer id = 1;

    Product productToUpdate = getProductForTestPurpose(id, "Product 1", "Product Description 1");
    when(productService.updateProduct(id, productToUpdate)).thenReturn(null);

    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/product/{id}", id)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .accept(MediaType.APPLICATION_JSON)
      .characterEncoding("UTF-8")
      .content(jsonForTestPurpose(productToUpdate));

    this.mockMvc.perform(builder).andExpect(status().isNotFound());
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

  private String jsonForTestPurpose(Product product) {
    ObjectMapper objectMapper = new ObjectMapper();
    String json = null;
    try {
        json = objectMapper.writeValueAsString(product);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return json;
  }
}
