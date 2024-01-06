package com.edu.artexhibit.models.dtos;

import com.edu.artexhibit.models.enums.ArtistCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    @JsonProperty("access_token")
    private String accessToken;
    private String username;
    private String profileImage;
    private ArtistCategory category;
}
