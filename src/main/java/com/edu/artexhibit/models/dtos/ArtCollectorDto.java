package com.edu.artexhibit.models.dtos;

import com.edu.artexhibit.models.entity.ArtistEntity;
import lombok.Data;

@Data
public class ArtCollectorDto {
    private long id;
    private PortfolioDto portfolio;
    private ArtistEntity artist;
    private ArtistEntity creator;
}
