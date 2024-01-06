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
@Table(name = "portfolio")
public class PortfolioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String jsonTheme;
    private Instant createdAt;
    private Instant updatedAt;
    @OneToMany(mappedBy = "portfolio", fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    private List<FileEntity> files;
    @OneToOne
    @JoinColumn(name = "artist_id")
    @JsonIgnore
    private ArtistEntity artist;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
