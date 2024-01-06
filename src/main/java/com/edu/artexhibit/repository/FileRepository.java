package com.edu.artexhibit.repository;

import com.edu.artexhibit.models.entity.FileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<FileEntity, Long>{}
