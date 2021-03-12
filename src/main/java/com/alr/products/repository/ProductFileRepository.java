package com.alr.products.repository;

import com.alr.products.entity.ProductFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductFileRepository extends JpaRepository<ProductFile, Integer> {

  Optional<ProductFile> findByName(String name);
}
