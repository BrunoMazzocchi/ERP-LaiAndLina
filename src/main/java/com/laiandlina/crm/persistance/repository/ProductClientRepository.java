package com.laiandlina.crm.persistance.repository;

import com.laiandlina.crm.persistance.data.*;
import com.laiandlina.crm.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface ProductClientRepository extends JpaRepository<ProductClient, Integer> {

     @Query(nativeQuery = true, value = "SELECT * FROM vw_active_order where state <> 5 ORDER BY start_date ")
     List<VwOrder> getVwActiveOrder();

     //A completed order <Order with a state 4 (completed by user)>
     @Query(nativeQuery = true, value = "SELECT * FROM vw_completed_order where state <> 5 ORDER BY start_date")
     List<VwOrder> getVwCompletedOrder();


     ProductClient save(ProductClient productClient);

     ProductClient findById(int order);

     @Query(nativeQuery = true, value = "SELECT * FROM vw_active_order where id = ?")
     VwOrder findOrderById(int orderId);

     @Query(nativeQuery = true, value = "SELECT * FROM vw_completed_order where id = ?")
     VwOrder findOrderByIdActive(int orderId);


     @Query(nativeQuery = true, value = "select count(*) as 'completed' from product_client where product_client.state = 4  and  start_date between ? and end_date;")
     int getOrderCompletedCount(String startDate);

     @Query(nativeQuery = true, value = "select count(*) as 'completed' from product_client where product_client.state < 4  and  start_date between ? and end_date;")
     int getOrderActiveCount(String startDate);

     @Query(nativeQuery = true, value = "SELECT\n" +
             "MONTH(start_date) as 'month',\n" +
             "COUNT(*) as 'count'\n" +
             "FROM product_client where product_client.state = 4 and  start_date between start_date and end_date\n" +
             "GROUP BY MONTH(start_date) order by month(start_date) asc;")
     HashSet<List<Integer>> getOrderCompletedPerMonth();

     @Query(nativeQuery = true, value = "SELECT\n" +
             "MONTH(start_date) as 'month',\n" +
             "COUNT(*) as 'count'\n" +
             "FROM product_client where product_client.state < 4 and  start_date between start_date and end_date\n" +
             "GROUP BY MONTH(start_date) order by month(start_date) asc")
     HashSet<List<Integer>> getOrderActivePerMonth();




}
