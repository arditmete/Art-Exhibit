package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.models.dtos.EventDto;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.models.entity.EventEntity;
import com.edu.artexhibit.repository.EventRepository;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.exceptions.ErrorCode;
import com.edu.artexhibit.service.IEntityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService implements IEntityService<EventDto> {

    private final EventRepository eventRepository;
    private final ArtistService artistService;
    private final ModelMapper modelMapper;
    private final MessageProperties messageProperties;
    @Override
    public EventDto findById(long id) {
        ArtistDto artist = artistService.getLoggedInUser();
        ArtistEntity artistEntity = modelMapper.map(artist, ArtistEntity.class);
        Optional<EventEntity> eventEntity = eventRepository.findByIdAndArtist(id, artistEntity);
        return eventEntity.map(entity -> modelMapper.map(entity, EventDto.class)).orElseThrow(()-> new ArtExhibitException(ErrorCode.EVENT_NOT_FOUND));
    }

    @Override
    public List<EventDto> findAll(int page, int size) {
        List<EventDto> eventDtos = new ArrayList<>();
        Pageable paging = PageRequest.of(page,size);
        Page<EventEntity> eventEntities = eventRepository.findAll(paging);
        eventEntities.forEach(
                eventEntity -> {
                    EventDto eventDto = modelMapper.map(eventEntity, EventDto.class);
                    eventDto.setUsername(eventEntity.getArtist().getUsername());
                    eventDtos.add(eventDto);
                }
        );
        return eventDtos;
    }

    @Override
    public EventDto findByName(String name) {
        EventEntity eventEntity = eventRepository.findByName(name).orElseThrow(()->new ArtExhibitException(ErrorCode.EVENT_NOT_FOUND));
        return modelMapper.map(eventEntity, EventDto.class);
    }

    @Override
    public EventDto update(EventDto eventDto, long id ) {
        ArtistDto artist = artistService.getLoggedInUser();
        ArtistEntity artistEntity = modelMapper.map(artist, ArtistEntity.class);
        EventEntity eventEntity = eventRepository.findByIdAndArtist(id, artistEntity).orElseThrow(()->new ArtExhibitException(ErrorCode.EVENT_NOT_FOUND));
        eventEntity.setUpdatedAt(Instant.now());
        eventEntity.setName(eventDto.getName());
        eventEntity = eventRepository.save(eventEntity);
        return modelMapper.map(eventEntity, EventDto.class);
    }

    @Override
    @Transactional
    public String delete(long id) {
        ArtistDto artist = artistService.getLoggedInUser();
        ArtistEntity artistEntity = modelMapper.map(artist, ArtistEntity.class);
        eventRepository.deleteByIdAndArtist(id, artistEntity);
        return messageProperties.getEventIsDeletedSuccessfully();
    }

    @Override
    public EventDto create(EventDto eventDto) {
        if (checkIfExists(eventDto)) {
            throw new ArtExhibitException(ErrorCode.EVENT_ALREADY_REGISTERED);
        }
        ArtistDto artistDto = artistService.getLoggedInUser();
        ArtistEntity artistEntity = modelMapper.map(artistDto, ArtistEntity.class);
        EventEntity eventEntity = modelMapper.map(eventDto, EventEntity.class);
        eventEntity.setArtist(artistEntity);
        eventEntity.setCreatedAt(Instant.now());
        eventRepository.save(eventEntity);
        return modelMapper.map(eventEntity, EventDto.class);
    }

    @Override
    public boolean checkIfExists(EventDto eventDto) {
        Optional<EventEntity> existingEvent = eventRepository.findByName(eventDto.getName());
        return existingEvent.isPresent();
    }
}
