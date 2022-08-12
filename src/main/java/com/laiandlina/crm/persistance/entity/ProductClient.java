package com.laiandlina.crm.persistance.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@Entity
@Data
public class ProductClient {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date startDate;
    private Date endDate;


    private Integer idClient;
    private Integer idProduct;

    private double basePrice;
    private double finalPrice;

    private Integer state;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @OneToMany(mappedBy="productClient")
    private Set<User> users;
}
