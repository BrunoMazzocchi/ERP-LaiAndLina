package com.laiandlina.crm.domain.service;

import com.laiandlina.crm.persistance.data.*;
import com.laiandlina.crm.persistance.entity.*;
import com.laiandlina.crm.persistance.repository.*;
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
    public VwOrder findById(int idOrder){
        return productClientRepository.findById(idOrder);
    }
}
