package com.edu.artexhibit.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NotificationDto {
    private Long id;
    @NotNull(message = "Title is required!")
    private String title;
    @NotNull(message = "Description is required!")
    private String description;
    private List<CommentDto> comments;
    private  ArtistDto artist;
    private Instant createdAt;
    private Instant updatedAt;
}
