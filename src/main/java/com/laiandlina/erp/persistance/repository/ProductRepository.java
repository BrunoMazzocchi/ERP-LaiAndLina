package com.laiandlina.erp.persistance.repository;



import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(nativeQuery = true, value = "select * from product where state <> 3")
    List<Product> findAll();
    Optional<Product>findById(int clientId);
    Product save(Product product);
}
