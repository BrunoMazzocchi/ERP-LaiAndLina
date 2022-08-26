package com.laiandlina.erp.persistance.data;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogOutRequest {
    private DeviceInfo deviceInfo;
    private String token;
}