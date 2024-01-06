package com.edu.artexhibit.models.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PortfolioDto {
    private long id;
    @NotNull
    private String name;
    private ArtistDto artist;
    private String jsonTheme;
    private List<FileDto> files;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<EventDto> eventDtos;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CategoryDto> categoryDtos;
}
