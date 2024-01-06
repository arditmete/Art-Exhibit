package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.models.dtos.NotificationDto;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.models.entity.NotificationEntity;
import com.edu.artexhibit.repository.NotificationRepository;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.exceptions.ErrorCode;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService implements IEntityService<NotificationDto> {

    private final ArtistService artistService;
    private final ModelMapper modelMapper;
    private final MessageProperties messageProperties;
    private final NotificationRepository notificationRepository;
    private final CommentService commentService;

    public NotificationDto findById(long id, int page, int size) {
        Optional<NotificationEntity> notificationEntity = notificationRepository.findById(id);
        if(notificationEntity.isPresent()){
            NotificationDto notificationDto = modelMapper.map(notificationEntity, NotificationDto.class);
            notificationDto.setComments(commentService.findByNotificationId(id, page, size));
            return notificationDto;
        }
        throw new ArtExhibitException(ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Override
    public NotificationDto findById(long id) {
       return new NotificationDto();
    }

    @Override
    public List<NotificationDto> findAll(int page, int size) {
        List<NotificationDto> notificationDtos = new ArrayList<>();
        Pageable paging = PageRequest.of(page,size);
        Page<NotificationEntity> notificationEntities = notificationRepository.findAll(paging);
        notificationEntities.forEach(
                notificationEntity -> notificationDtos.add(modelMapper.map(notificationEntity, NotificationDto.class)));
        return notificationDtos;
    }

    @Override
    public NotificationDto findByName(String name){
       return new NotificationDto();
    }

    @Override
    public NotificationDto update(NotificationDto notificationDto, long id ) {
        NotificationEntity notificationEntity = notificationRepository.findById(id).orElseThrow(()->new ArtExhibitException(ErrorCode.NOTIFICATION_NOT_FOUND));
        notificationEntity.setUpdatedAt(Instant.now());
        notificationEntity.setDescription(notificationDto.getDescription());
        notificationEntity.setTitle(notificationDto.getTitle());
        notificationEntity = notificationRepository.save(notificationEntity);
        return modelMapper.map(notificationEntity, NotificationDto.class);
    }

    @Override
    public String delete(long id) {
        NotificationDto notificationDto = findById(id);
        NotificationEntity notificationEntity = modelMapper.map(notificationDto, NotificationEntity.class);
        notificationRepository.delete(notificationEntity);
        return messageProperties.getCategoryIsDeletedSuccessfully();
    }

    @Override
    public NotificationDto create(NotificationDto notificationDto) {

        ArtistDto artistDto = artistService.getLoggedInUser();
        ArtistEntity artistEntity = modelMapper.map(artistDto, ArtistEntity.class);
        NotificationEntity notificationEntity = modelMapper.map(notificationDto, NotificationEntity.class);
        notificationEntity.setArtist(artistEntity);
        notificationEntity.setCreatedAt(Instant.now());
        notificationRepository.save(notificationEntity);
        return modelMapper.map(notificationEntity, NotificationDto.class);
    }

    @Override
    public boolean checkIfExists(NotificationDto notificationDto) {
       return false;
    }
}
