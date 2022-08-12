package com.laiandlina.crm.persistance.entity;

import lombok.*;

import javax.persistence.*;
@Entity
@Data
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
