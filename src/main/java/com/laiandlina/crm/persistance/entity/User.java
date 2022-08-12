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
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Integer id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private  String email;
    @Column
    private String password;
    @Column
    private String urlPhoto;
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
    private Integer state;
    @Column
    private String phoneNumber;
    @Column
    private String department;
    @Column(nullable = false)
    private Boolean active;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_department",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "departmentId"))
    private Set<Department> departments = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="idProductClient", nullable=false)
    private ProductClient productClient;


    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
