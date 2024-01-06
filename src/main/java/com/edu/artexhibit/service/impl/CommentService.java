package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.exceptions.ErrorCode;
import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.models.dtos.CommentDto;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.models.entity.CommentEntity;
import com.edu.artexhibit.models.entity.NotificationEntity;
import com.edu.artexhibit.repository.CommentRepository;
import com.edu.artexhibit.repository.NotificationRepository;
import com.edu.artexhibit.service.IEntityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements IEntityService<CommentDto> {

    private final ArtistService artistService;
    private final ModelMapper modelMapper;
    private final MessageProperties messageProperties;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;

    // find by notification ID
    public List<CommentDto> findByNotificationId(long id, int page, int size) {
        Pageable paging = PageRequest.of(page,size);
        NotificationEntity notificationEntity = notificationRepository.findById(id).orElseThrow(()->new ArtExhibitException(ErrorCode.NOTIFICATION_NOT_FOUND));
        Page<CommentEntity> commentEntities = commentRepository.findByNotification(notificationEntity, paging);
        List<CommentDto> commentDtos = new ArrayList<>();
        commentEntities.forEach(commentEntity -> commentDtos.add(modelMapper.map(commentEntity, CommentDto.class)));
        return commentDtos;
    }

    @Override
    public CommentDto findById(long id) {
       return new CommentDto();
    }

    @Override
    public List<CommentDto> findAll(int page, int size) {
        return new ArrayList<>();
    }

    @Override
    public CommentDto findByName(String name){
       return new CommentDto();
    }

    @Override
    public CommentDto update(CommentDto commentDto, long id ) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(()->new ArtExhibitException(ErrorCode.COMMENT_NOT_FOUND));
        commentEntity.setUpdatedAt(Instant.now());
        commentEntity.setDescription(commentDto.getDescription());
        commentEntity.setTitle(commentDto.getTitle());
        commentEntity = commentRepository.save(commentEntity);
        return modelMapper.map(commentEntity, CommentDto.class);
    }

    @Override
    public String delete(long id) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(()->new ArtExhibitException(ErrorCode.COMMENT_NOT_FOUND));
        commentRepository.delete(commentEntity);
        return messageProperties.getCommentIsDeletedSuccessfully();
    }

    @Override
    public CommentDto create(CommentDto commentDto) {
        NotificationEntity notificationEntity = notificationRepository.findById(commentDto.getNotificationId()).orElseThrow(()->new ArtExhibitException(ErrorCode.NOTIFICATION_NOT_FOUND));
        ArtistDto artistDto = artistService.getLoggedInUser();
        ArtistEntity artistEntity = modelMapper.map(artistDto, ArtistEntity.class);
        CommentEntity commentEntity = modelMapper.map(commentDto, CommentEntity.class);
        commentEntity.setArtist(artistEntity);
        commentEntity.setCreatedAt(Instant.now());
        commentEntity.setNotification(notificationEntity);
        commentRepository.save(commentEntity);
        return modelMapper.map(commentEntity, CommentDto.class);
    }

    @Override
    public boolean checkIfExists(CommentDto commentDto) {
       return false;
    }
}
