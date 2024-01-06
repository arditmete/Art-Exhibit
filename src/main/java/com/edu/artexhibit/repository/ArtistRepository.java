package com.edu.artexhibit.repository;

import com.edu.artexhibit.models.entity.ArtistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends CrudRepository<ArtistEntity, Long> {
    Optional<ArtistEntity> findByUsername(String username);
    Optional<ArtistEntity> findByEmail(String email);
    Page<ArtistEntity> findAll(Pageable pageable);
}
