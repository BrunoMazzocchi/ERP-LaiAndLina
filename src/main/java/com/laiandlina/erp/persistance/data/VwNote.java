package com.laiandlina.erp.persistance.data;

import java.sql.*;

public interface VwNote {
    int getNoteId();
    String getTitle();
    String getDescription();
    int getIdProductClient();
    int getIdUser();
    String getUserName();
    String getUserPhotoURL();
    Date getPostedDate();

}
