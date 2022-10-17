package com.oracle.product.dao;

import com.oracle.product.entity.ProductDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;


public interface ProductRepository extends JpaRepository<ProductDetailsEntity, Long> {

    List<ProductDetailsEntity> findByNameContainingIgnoreCaseOrderByNameAsc(String name);


}
