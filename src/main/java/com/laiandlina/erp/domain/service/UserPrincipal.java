package com.laiandlina.erp.domain.service;

import com.fasterxml.jackson.annotation.*;
import com.laiandlina.erp.persistance.entity.*;
import com.laiandlina.erp.persistance.entity.User;
import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;
@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal  implements  UserDetails{
    private static final Integer serialVersionUID = 1;
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String urlPhoto;
    private String phoneNumber;
    private Set<Role> role;
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private Map<String, Object> attributes;

    public UserPrincipal(int id, String firstName, String lastName,
                         String email, String password, String urlPhoto,
                         String phoneNumber, Set<Role> role,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.urlPhoto = urlPhoto;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public static UserPrincipal build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getRoleName().name())
        ).collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getUrlPhoto(),
                user.getPhoneNumber(),
                user.getRoles(),
                authorities
        );
    }
    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public Set<Role> getRole(){
        return role;
    }

    public String getEmail() {
        return email;
    }

    public  String getUrlPhoto(){
        return urlPhoto;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrincipal user = (UserPrincipal) o;
        return Objects.equals(id, user.id);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}