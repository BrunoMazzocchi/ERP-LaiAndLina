package com.laiandlina.erp.persistance.data;

import lombok.*;
import org.springframework.web.multipart.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadPictureForm {
    private MultipartFile myPicture;
}
