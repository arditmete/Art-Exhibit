package com.edu.artexhibit.repository;

import com.edu.artexhibit.models.entity.CommentEntity;
import com.edu.artexhibit.models.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, Long> {

    Page<CommentEntity> findAll(Pageable pageable);
    Page<CommentEntity> findByNotification(NotificationEntity notificationEntity, Pageable pageable);

}
