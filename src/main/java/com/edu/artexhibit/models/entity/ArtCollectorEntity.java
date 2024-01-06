package com.edu.artexhibit.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "art_collector")
public class ArtCollectorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private PortfolioEntity portfolio;
    @ManyToOne
    private ArtistEntity artist;
    @ManyToOne
    private ArtistEntity creator;
}
