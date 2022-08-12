package com.laiandlina.crm.persistance.entity;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    private Integer id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private Integer userCreator;
    @Column
    private Date creationDate;
    @Column
    private Integer userMod;
    @Column
    private Date modDate;
    @Column
    private Integer userDelete;
    @Column
    private Date deleteDate;
    @Column
    private Double price;
    @Column
    private Integer state;
}
