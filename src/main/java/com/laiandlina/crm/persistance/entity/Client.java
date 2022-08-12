package com.laiandlina.crm.persistance.entity;

import javax.persistence.*;

public class Client {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    private Integer id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String identification;
}
