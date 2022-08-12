package com.laiandlina.crm.persistance.data;

import lombok.*;

@Data
public class NewProductForm {
    private Integer id;
    private String name;
    private String description;
    private  Double price;
}
