package com.laiandlina.crm.persistance.data;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshRequest {
    private String refreshToken;
}