package com.edu.artexhibit.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.Instant;

@Data
public class FileDto {
    private Long id;
    private String content;
    private Instant createdAt;
    @JsonIgnore
    private PortfolioDto portfolio;
}
