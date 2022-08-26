package com.laiandlina.erp.persistance.data;

import java.sql.*;

public interface VwUserDepartment {
    int getId();
    String getFirstName();
    String getLastName();
    String getDepartmentName();
    String getEmail();
    String getPhoneNumber();
    String getState();
    Date getCreationDate();
    String getUrlPhoto();
}
