package com.alr.products.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

  @Id
  @Column(name = "im", insertable = true, updatable = false)
  @NotNull(message = "The field Id is mandatory")
  private Integer id;

  @Column(name = "category_id", insertable = true, updatable = false)
  @NotNull(message = "The field Category is mandatory")
  private Integer category;

  @NotBlank(message = "The field Name is mandatory")
  private String name;

  @NotNull(message = "The field Free Shipping is mandatory")
  private Boolean freeShipping;

  @NotBlank(message = "The field Description is mandatory")
  private String description;

  @Column(name = "price", precision = 2)
  @NotNull(message = "The field Price is mandatory")
  private Double price;
}

