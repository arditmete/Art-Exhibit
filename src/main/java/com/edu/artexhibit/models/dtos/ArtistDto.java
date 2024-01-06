package com.edu.artexhibit.models.dtos;

import com.edu.artexhibit.models.enums.ArtistCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDto {
    private Long id;
    @NotNull(message = "Firstname is required!")
    private String firstName;
    @NotNull(message = "Category is required!")
    private ArtistCategory category;
    @NotNull(message = "LastName is required!")
    private String lastName;
    private Boolean student;
    @NotNull
    @Email(message = "Email is not correct!")
    private String email;
    @NotNull(message = "Username is required!")
    private String username;
    @Size(min = 8, message = "Password is too short. It needs to be at least 8 characters.")
    @JsonIgnore
    private String password;
    private String description;
    private String role;
    @NotNull(message = "Birth of date is required!")
    private LocalDate birthOfDate;
    private Instant createdAt;
    private Instant updatedAt;
    @NotNull(message = "Address is not correct!")
    private String address;
    @NotNull(message = "Phone number is not correct!")
    private String phoneNumber;
    @NotNull(message = "Image profile is required!")
    private String profileImage;
    private PortfolioDto portfolio;
    @JsonIgnore
    private List<NotificationDto> notification;

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }
    @JsonProperty
    public void setPassword(final String password) {
        this.password = password;
    }

}
