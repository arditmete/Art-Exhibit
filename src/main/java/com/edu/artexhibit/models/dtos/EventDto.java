package com.edu.artexhibit.models.dtos;


import com.edu.artexhibit.utils.ImageUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventDto {
    private long id;
    @NotNull(message = "Name is required!")
    private String name;
    @NotNull(message = "Event time is required!")
    private LocalDateTime time;
    @NotNull(message = "Image is required!")
    private String photo;
    @NotNull(message = "Address is required!")
    private String address;
    @NotNull(message = "Description is required!")
    private String description;
    @NotNull(message = "Category is required!")
    private String category;
    private String username;

}
