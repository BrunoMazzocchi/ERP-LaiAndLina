package com.laiandlina.crm.persistance.data;

import java.sql.*;

public interface VwPost {
    String getId();
    Date getDate();
    String getTitle();
    String getDescription();
    String getUserPost();
    String getUrl_photo();
    int getUser();
    String getUserEmail();
}
