package com.laiandlina.erp.persistance.repository;

import com.laiandlina.erp.persistance.data.*;
import com.laiandlina.erp.persistance.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface ProductClientRepository extends JpaRepository<ProductClient, Integer> {

     @Query(nativeQuery = true, value = "SELECT * FROM vw_active_order where state <> 5 ORDER BY start_date ")
     List<VwOrder> getVwActiveOrder();

     @Query(nativeQuery = true, value = "SELECT * FROM vw_active_order where state = 0 ORDER BY start_date ")
     List<VwOrder> getVwActiveStarted();

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
     @Query(nativeQuery = true, value = "select count(*) as 'completed' from product_client where product_client.state = 0")
     int getStartedOrder();
     @Query(nativeQuery = true, value = "select sum(final_price) from product_client where year (start_date) = ? and state = 4;")
     double getSumAllMonth(int year);

     @Query(nativeQuery = true, value = "SELECT sum(product_client.final_price) as 'sum'\n" +
             "FROM (select 1 as mon union all select 2 union all select 3 union all select 4 union all\n" +
             "      select 5 union all select 6 union all select 7 union all select 8 union all\n" +
             "      select 9 union all select 10 union all select 11 union all select 12\n" +
             "     ) m left outer join\n" +
             "     product_client\n" +
             "     on m.mon = month(start_date) and year(start_date) = ?\n" +
             "GROUP BY m.mon order by mon asc;\n" +
             "\n")
     List<Integer> getOrderCompletedPerMonth(int year);

     @Query(nativeQuery = true, value = "SELECT\n" +
             "MONTH(start_date) as 'month',\n" +
             "COUNT(*) as 'count'\n" +
             "FROM product_client where product_client.state < 4 and  start_date between start_date and end_date\n" +
             "GROUP BY MONTH(start_date) order by month(start_date) asc")
     HashSet<List<Integer>> getOrderActivePerMonth();
}
