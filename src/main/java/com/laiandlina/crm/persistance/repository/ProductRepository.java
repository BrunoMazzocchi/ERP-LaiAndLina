package com.laiandlina.crm.persistance.repository;



import com.laiandlina.crm.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAll();
    Optional<Product>findById(int clientId);
    Product save(Product product);
}
