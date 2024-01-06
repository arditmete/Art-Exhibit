package com.edu.artexhibit.models.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CategoryDto {
    private long id;
    @NotNull
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PortfolioDto portfolio;
}
