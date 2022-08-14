package com.laiandlina.crm.persistance.data;

import java.sql.Date;


public interface VwOrder {
     Integer getId();
     Double getBasePrice();
     Double getFinal_price();
     Date getStartDate();
     Date getEndDate();
     int getState();
     String getClient();
     String getProduct();
}
