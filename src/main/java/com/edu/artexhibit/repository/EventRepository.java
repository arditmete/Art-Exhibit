package com.edu.artexhibit.repository;

import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.models.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends CrudRepository<EventEntity, Long> {
    Optional<EventEntity> findByName(String name);
    void deleteByIdAndArtist(long id, ArtistEntity artist);
    Optional<EventEntity> findByIdAndArtist(long id, ArtistEntity artist);
    List<EventEntity> findByArtist(ArtistEntity artist);
    Page<EventEntity> findByArtist(ArtistEntity artist, Pageable pageable);
    Page<EventEntity> findAll(Pageable pageable);
}
