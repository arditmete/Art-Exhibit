package com.edu.artexhibit.models.dtos;

import lombok.Data;

import java.time.Instant;
@Data
public class CommentDto {
    private Long id;
    private String title;
    private String description;
    private long notificationId;
    private ArtistDto artist;
    private Instant createdAt;
    private Instant updatedAt;
}
