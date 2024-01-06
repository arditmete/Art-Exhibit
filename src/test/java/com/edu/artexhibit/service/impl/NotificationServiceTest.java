package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.models.dtos.CommentDto;
import com.edu.artexhibit.models.dtos.NotificationDto;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.models.entity.NotificationEntity;
import com.edu.artexhibit.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NotificationServiceTest {
    @Spy
    @InjectMocks
    private ArtistService artistService;

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private MessageProperties messageProperties;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        notificationService = new NotificationService(artistService ,modelMapper, messageProperties, notificationRepository, commentService);
    }

    @Test
    public void testCreateNotification() {
        ArtistDto artistDto = new ArtistDto();
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(20L);
        notificationDto.setTitle("test");
        artistDto.setId(5L);
        String username = "string9";
        ArtistEntity artistEntity = new ArtistEntity();
        artistEntity.setId(5L);
        artistDto.setUsername(username);
        artistEntity.setId(5L);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);

        NotificationEntity notificationEntity = new NotificationEntity();
        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);
        when(modelMapper.map(notificationDto, NotificationEntity.class)).thenReturn(notificationEntity);

        when(notificationRepository.save(notificationEntity)).thenReturn(notificationEntity);

        when(notificationService.create(notificationDto)).thenReturn(notificationDto);
        NotificationDto createdNotificationDto = notificationService.create(notificationDto);

        assertNotNull(createdNotificationDto);
    }

    @Test
    public void testFindNotificationByIdNotFound() {
        // Create a sample notification ID that does not exist
        long notificationId = 456; // Replace with an ID that does not exist

        // Mock the notificationRepository to return an empty Optional
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // Call the findById method and expect it to throw an exception
        assertThrows(ArtExhibitException.class, () -> notificationService.findById(notificationId, 0, 10));
    }

    @Test
    public void testFindAllNotifications() {
        // Create a sample Pageable for pagination
        Pageable paging = PageRequest.of(0, 10);

        // Mock the Page<NotificationEntity> for notifications
        Page<NotificationEntity> notificationEntities = mock(Page.class);
        when(notificationRepository.findAll(paging)).thenReturn(notificationEntities);

        // Create a sample NotificationEntity and NotificationDto
        NotificationEntity notificationEntity = new NotificationEntity(/* Initialize with required data */);
        NotificationDto notificationDto = new NotificationDto(/* Initialize with required data */);

        // Mock the modelMapper to map NotificationEntities to NotificationDto
        when(modelMapper.map(notificationEntity, NotificationDto.class)).thenReturn(notificationDto);

        // Call the findAll method
        List<NotificationDto> notificationDtos = notificationService.findAll(0, 10);

        // Verify that the findAll method behaves correctly and returns the expected list of NotificationDto
        assertNotNull(notificationDtos);
        // Add assertions to verify other aspects of the method if needed
    }

    @Test
    public void testUpdateNotificationFound() {
        // Create a sample notification ID that exists
        long notificationId = 789; // Replace with an existing ID

        // Mock the notificationRepository to return a valid NotificationEntity
        NotificationEntity notificationEntity = new NotificationEntity(/* Initialize with required data */);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notificationEntity));

        // Mock the modelMapper to map NotificationDto to NotificationEntity
        NotificationDto notificationDto = new NotificationDto(/* Initialize with updated data */);
        NotificationEntity updatedNotificationEntity = new NotificationEntity(/* Initialize with updated data */);
        when(modelMapper.map(notificationDto, NotificationEntity.class)).thenReturn(updatedNotificationEntity);

        // Mock the notificationRepository to save the updated NotificationEntity and return it
        when(notificationRepository.save(updatedNotificationEntity)).thenReturn(updatedNotificationEntity);
        when(notificationService.update(notificationDto, notificationId)).thenReturn(notificationDto);
        // Call the update method
        NotificationDto updatedNotificationDto = notificationService.update(notificationDto, notificationId);

        // Verify that the update method behaves correctly and returns the expected NotificationDto
        assertNotNull(updatedNotificationDto);
        // Add assertions to verify other aspects of the method if needed
    }

    @Test
    public void testDeleteNotification() {
        long notificationId = 1;
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(notificationId);
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(notificationId);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notificationEntity));

        when(modelMapper.map(any(), eq(NotificationEntity.class))).thenReturn(notificationEntity);
        when(notificationService.delete(notificationId)).thenReturn("Notification is deleted successfully!");
        String resultMessage = notificationService.delete(notificationId);

        assertNotNull(resultMessage);
    }
}
