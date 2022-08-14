package com.laiandlina.crm.persistance.repository;

import com.laiandlina.crm.persistance.data.*;
import com.laiandlina.crm.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface ProductClientRepository extends JpaRepository<ProductClient, Integer> {
     ProductClient findById(int orderId);

     @Query(nativeQuery = true, value = "SELECT * FROM vw_active_order ORDER BY start_date")
     List<VwOrder> getVwActiveOrder();

     //A completed order <Order with a state 4 (completed by user)>
     @Query(nativeQuery = true, value = "SELECT * FROM vw_completed_order ORDER BY start_date")
     List<VwOrder> getVwCompletedOrder();


}
