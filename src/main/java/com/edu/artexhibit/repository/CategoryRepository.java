package com.edu.artexhibit.repository;

import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.models.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByName(String name);
    List<CategoryEntity> findByArtist(ArtistEntity artist);
    Page<CategoryEntity> findByArtist(ArtistEntity artist, Pageable pageable);
    Optional<CategoryEntity> findByIdAndArtist(long id, ArtistEntity artist);
}
