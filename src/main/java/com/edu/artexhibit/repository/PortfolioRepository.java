package com.edu.artexhibit.repository;

import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.models.entity.PortfolioEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends CrudRepository<PortfolioEntity, Long> {
    Optional<PortfolioEntity> findByName(String name);
    void deleteByIdAndArtist(long id, ArtistEntity artist);
    Optional<PortfolioEntity> findByArtist(ArtistEntity artist);
}
