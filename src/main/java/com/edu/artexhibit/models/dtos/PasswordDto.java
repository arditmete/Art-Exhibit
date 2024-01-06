package com.edu.artexhibit.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PasswordDto {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
