package com.laiandlina.erp.domain.service;

import com.laiandlina.erp.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product>findAll(){
        return productRepository.findAll();
    }
    public Optional<Product> findById(int idProduct){
        return productRepository.findById(idProduct);
    }
    public Product save(Product product){
        return productRepository.save(product);
    }
}
