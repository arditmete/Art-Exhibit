package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.models.dtos.EventDto;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.models.entity.EventEntity;
import com.edu.artexhibit.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventServiceTest {

    @InjectMocks
    private ArtistService artistService;

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private MessageProperties messageProperties;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        eventService = new EventService(eventRepository, artistService, modelMapper, messageProperties);
    }

    @Test
    public void testCreateEventExist() {
        EventDto eventDto = new EventDto();

        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(5L);
        ArtistEntity artistEntity = new ArtistEntity();
        artistEntity.setId(5L);
        EventEntity eventEntity = new EventEntity();
        eventEntity.setName("test");
        artistEntity.setId(5L);
        String username = "string9";
        artistDto.setUsername(username);
        artistEntity.setId(5L);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);
        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);
        when(modelMapper.map(eventDto, EventEntity.class)).thenReturn(eventEntity);

        EventEntity savedEventEntity = new EventEntity();
        when(eventRepository.save(eventEntity)).thenReturn(savedEventEntity);
        when(eventService.create(eventDto)).thenReturn(eventDto);
        EventDto createdEventDto = eventService.create(eventDto);

        assertNotNull(createdEventDto);
    }
    @Test
    public void testFindEventByIdFound() {
        // Create a sample event ID that exists
        long eventId = 1; // Replace with an existing ID
        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(5L);
        String username = "string9";
        ArtistEntity artistEntity = new ArtistEntity();
        artistDto.setUsername(username);
        artistEntity.setId(5L);
        artistEntity.setUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);
        // Mock the artistService to return a valid ArtistDto
        when(artistService.getLoggedInUser()).thenReturn(artistDto);

        // Mock the modelMapper to map ArtistDto to ArtistEntity
        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);

        // Mock the eventRepository to return a valid EventEntity
        EventEntity eventEntity = new EventEntity(/* Initialize with required data */);
        when(eventRepository.findByIdAndArtist(eventId, artistEntity)).thenReturn(Optional.of(eventEntity));

        // Mock the modelMapper to map EventEntity to EventDto
        EventDto eventDto = new EventDto(/* Initialize with required data */);
        when(modelMapper.map(eventEntity, EventDto.class)).thenReturn(eventDto);

        // Call the findById method
        EventDto foundEventDto = eventService.findById(eventId);

        // Verify that the findById method behaves correctly and returns the expected EventDto
        assertNotNull(foundEventDto);
        // Add assertions to verify other aspects of the method if needed
    }

    @Test
    public void testFindEventByIdNotFound() {
        // Create a sample event ID that does not exist
        long eventId = 456; // Replace with an ID that does not exist
        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(5L);
        String username = "string9";
        ArtistEntity artistEntity = new ArtistEntity();
        artistDto.setUsername(username);
        artistEntity.setId(5L);
        artistEntity.setUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);
        // Mock the artistService to return a valid ArtistDto
        artistDto.setId(5L);
        artistDto.setUsername("string9");
        when(artistService.getLoggedInUser()).thenReturn(artistDto);

        // Mock the modelMapper to map ArtistDto to ArtistEntity
        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);

        // Mock the eventRepository to return an empty Optional
        when(eventRepository.findByIdAndArtist(eventId, artistEntity)).thenReturn(Optional.empty());

        // Call the findById method and expect it to throw an exception
        assertThrows(ArtExhibitException.class, () -> eventService.findById(eventId));
    }

    @Test
    public void testFindAllEvents() {
        // Create a sample Pageable for pagination
        PageRequest paging = PageRequest.of(0, 10);

        Page<EventEntity> eventEntities = mock(Page.class);
        when(eventRepository.findAll(paging)).thenReturn(eventEntities);

        // Create a sample EventEntity and EventDto
        EventEntity eventEntity = new EventEntity();
        EventDto eventDto = new EventDto();

        when(modelMapper.map(eventEntity, EventDto.class)).thenReturn(eventDto);
        List<EventDto> eventDtos = eventService.findAll(0, 10);

        assertNotNull(eventDtos);
    }

    @Test
    public void testFindEventByNameFound() {
        // Create a sample event name that exists
        String eventName = "Sample Event Name"; // Replace with an existing name

        // Mock the eventRepository to return a valid EventEntity
        EventEntity eventEntity = new EventEntity(/* Initialize with required data */);
        when(eventRepository.findByName(eventName)).thenReturn(Optional.of(eventEntity));

        // Mock the modelMapper to map EventEntity to EventDto
        EventDto eventDto = new EventDto(/* Initialize with required data */);
        when(modelMapper.map(eventEntity, EventDto.class)).thenReturn(eventDto);

        // Call the findByName method
        EventDto foundEventDto = eventService.findByName(eventName);

        // Verify that the findByName method behaves correctly and returns the expected EventDto
        assertNotNull(foundEventDto);
        // Add assertions to verify other aspects of the method if needed
    }

    @Test
    public void testFindEventByNameNotFound() {
        // Create a sample event name that does not exist
        String eventName = "Non-Existent Event Name"; // Replace with a name that does not exist

        // Mock the eventRepository to return an empty Optional
        when(eventRepository.findByName(eventName)).thenReturn(Optional.empty());

        // Call the findByName method and expect it to throw an exception
        assertThrows(ArtExhibitException.class, () -> eventService.findByName(eventName));
    }

    @Test
    public void testUpdateEventFound() {
        long eventId = 1;
        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(5L);
        String username = "string9";
        ArtistEntity artistEntity = new ArtistEntity();
        artistDto.setUsername(username);
        artistEntity.setId(5L);
        artistEntity.setUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);

        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);

        EventEntity eventEntity = new EventEntity();
        when(eventRepository.findByIdAndArtist(eventId, artistEntity)).thenReturn(Optional.of(eventEntity));

        EventDto eventDto = new EventDto();
        eventDto.setName("test");
        EventEntity updatedEventEntity = new EventEntity();
        updatedEventEntity.setName("test");
        when(modelMapper.map(eventDto, EventEntity.class)).thenReturn(updatedEventEntity);

        when(eventRepository.save(updatedEventEntity)).thenReturn(updatedEventEntity);
        when( eventService.update(eventDto, eventId)).thenReturn(eventDto);
        EventDto updatedEventDto = eventService.update(eventDto, eventId);

        assertNotNull(updatedEventDto);
    }

    @Test
    public void testUpdateEventNotFound() {
        long eventId = 101;
        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(5L);
        String username = "string9";
        ArtistEntity artistEntity = new ArtistEntity();
        artistDto.setUsername(username);
        artistEntity.setId(5L);
        artistEntity.setUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);
        // Mock the artistService to return a valid ArtistDto
        artistDto.setId(5L);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);

        artistEntity.setId(5L);
        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);

        // Mock the eventRepository to return an empty Optional
        when(eventRepository.findByIdAndArtist(eventId, artistEntity)).thenReturn(Optional.empty());

        // Call the update method and expect it to throw an exception
        assertThrows(ArtExhibitException.class, () -> eventService.update(new EventDto(), eventId));
    }

    @Test
    public void testDeleteEvent() {
        long eventId = 1;
        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(5L);
        String username = "string9";
        ArtistEntity artistEntity = new ArtistEntity();
        artistDto.setUsername(username);
        artistEntity.setId(5L);
        artistEntity.setUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);

        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);
        when(eventService.delete(eventId)).thenReturn("Event is deleted successfully!");
        String resultMessage = eventService.delete(eventId);

        assertNotNull(resultMessage);
    }
}
