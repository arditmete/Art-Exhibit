package com.edu.artexhibit.models.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Instant createdAt;
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private ArtistEntity artist;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private PortfolioEntity portfolio;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
