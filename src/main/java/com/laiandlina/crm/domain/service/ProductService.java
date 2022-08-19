package com.laiandlina.crm.domain.service;

import com.laiandlina.crm.persistance.entity.*;
import com.laiandlina.crm.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.parameters.*;
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
