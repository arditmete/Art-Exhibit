package com.edu.artexhibit.repository;

import com.edu.artexhibit.models.entity.ArtCollectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtCollectorRepository extends JpaRepository<ArtCollectorEntity, Long> {
    Optional<ArtCollectorEntity> findByPortfolioIdAndArtistId(long portfolioId, long artistId);
    List<ArtCollectorEntity> findAllByArtistId(long id);
}
