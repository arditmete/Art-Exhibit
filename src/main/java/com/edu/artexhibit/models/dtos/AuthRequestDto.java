package com.edu.artexhibit.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {
    private String username;
    @Size(min = 8, message = "Password is too short. It needs to be at least 8 characters.")
    private String password;
}
