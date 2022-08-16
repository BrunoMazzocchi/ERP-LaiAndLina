package com.laiandlina.crm.persistance.data;

import lombok.*;
import org.springframework.web.multipart.*;

import java.io.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadPictureForm {
    private MultipartFile myPicture;
}
