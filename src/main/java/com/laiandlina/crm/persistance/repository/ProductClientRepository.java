package com.laiandlina.crm.persistance.repository;

import com.laiandlina.crm.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface ProductClientRepository extends JpaRepository<ProductClient, Integer> {
     ProductClient findById(int orderId);


}
