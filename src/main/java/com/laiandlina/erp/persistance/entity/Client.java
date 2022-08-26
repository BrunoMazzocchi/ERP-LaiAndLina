package com.laiandlina.erp.persistance.entity;

import lombok.*;

import javax.persistence.*;
@Entity
@Data
public class Client {
    @Id
    @Column
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String identification;
    @Column
    private Integer state;
}
