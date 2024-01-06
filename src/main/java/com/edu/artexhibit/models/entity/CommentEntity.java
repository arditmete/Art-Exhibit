package com.edu.artexhibit.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant createdAt;
    private Instant updatedAt;

    private String title;

    private String description;
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private ArtistEntity artist;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private NotificationEntity notification;
}
