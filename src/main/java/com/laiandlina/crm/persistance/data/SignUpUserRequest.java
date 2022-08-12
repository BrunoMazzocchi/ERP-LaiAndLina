package com.laiandlina.crm.persistance.data;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpUserRequest
{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String department;
    private String phoneNumber;
}
