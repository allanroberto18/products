package com.alr.products.repository;

import com.alr.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
  Optional<Product> findByName(String name);
  List<Product> findAllByCategory(Integer category);
}
