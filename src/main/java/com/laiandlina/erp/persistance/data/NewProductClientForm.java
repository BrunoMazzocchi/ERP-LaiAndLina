package com.laiandlina.erp.persistance.data;

import com.sun.istack.*;
import lombok.*;

import java.sql.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewProductClientForm {
    private Integer id;
    @NotNull
    private Integer idClient;
    @NotNull
    Integer idProduct;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private Integer progress;
    @NotNull
    private Integer state;
    @NotNull
    private Double basePrice;
    @NotNull
    private Double finalPrice;
    private Double salePrice;
    private String user;
}
