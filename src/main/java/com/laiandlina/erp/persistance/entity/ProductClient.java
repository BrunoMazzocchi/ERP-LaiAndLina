package com.laiandlina.erp.persistance.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

@Entity
@Data
public class ProductClient {
    @Id
    @Column
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date startDate;
    private Date endDate;


    private Integer idClient;
    private Integer idProduct;

    private double basePrice;
    private double finalPrice;

    private Integer state;

    @ManyToOne
    @JoinColumn(name = "idProduct", updatable = false, insertable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "idClient", updatable = false, insertable = false)
    private Client client;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_follow_product_client",
            joinColumns = @JoinColumn(name = "productClientId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<User> users = new HashSet<>();
}
