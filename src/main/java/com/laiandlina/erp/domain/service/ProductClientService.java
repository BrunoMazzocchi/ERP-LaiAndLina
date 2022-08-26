package com.laiandlina.erp.domain.service;

import com.laiandlina.erp.persistance.data.*;
import com.laiandlina.erp.persistance.entity.*;
import com.laiandlina.erp.persistance.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ProductClientService {
    @Autowired
    private ProductClientRepository productClientRepository;


    public List<VwOrder> findAllActiveOrders(){
        return productClientRepository.getVwActiveOrder();
    }
    public List<VwOrder> findAllCompletedOrders(){
        return productClientRepository.getVwCompletedOrder();
    }

    public ProductClient save(ProductClient productClient){
        return productClientRepository.save(productClient);
    }
    public VwOrder findOrderById(int idOrder){
        return productClientRepository.findOrderById(idOrder);
    }

    public ProductClient findById(int idOrder){
        return productClientRepository.findById(idOrder);
    }
}
