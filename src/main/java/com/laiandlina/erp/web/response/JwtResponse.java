package com.laiandlina.erp.web.response;

import lombok.*;

@Data
@NoArgsConstructor
public class JwtResponse {

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Long expiryDuration;

    public JwtResponse(String accessToken, String refreshToken, Long expiryDuration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiryDuration = expiryDuration;
        tokenType = "Bearer ";
    }
}