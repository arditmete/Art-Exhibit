package com.edu.artexhibit.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant createdAt;
    private Instant updatedAt;

    private String title;

    private String description;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private ArtistEntity artist;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "notification",  fetch = FetchType.EAGER)
    private List<CommentEntity> comments;
}
