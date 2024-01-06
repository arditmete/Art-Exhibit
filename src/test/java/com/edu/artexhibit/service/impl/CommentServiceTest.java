package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.models.dtos.CommentDto;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.models.entity.CommentEntity;
import com.edu.artexhibit.models.entity.NotificationEntity;
import com.edu.artexhibit.repository.CommentRepository;
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

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentServiceTest {
    @Spy
    @InjectMocks
    private ArtistService artistService;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private MessageProperties messageProperties;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        commentService = new CommentService(artistService ,modelMapper, messageProperties, commentRepository, notificationRepository);
    }

    @Test
    public void testCreateCommentNotificationFound() {
        CommentDto commentDto = new CommentDto();
        commentDto.setTitle("test");
        commentDto.setDescription("test");
        ArtistEntity artistEntity = new ArtistEntity();
        ArtistDto artistDto = new ArtistDto();
        artistEntity.setId(5L);
        String username = "String9";
        artistDto.setUsername(username);
        artistEntity.setId(5L);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);
        NotificationEntity notificationEntity = new NotificationEntity();
        when(notificationRepository.findById(commentDto.getNotificationId())).thenReturn(Optional.of(notificationEntity));

        when(artistService.getLoggedInUser()).thenReturn(artistDto);

        CommentEntity commentEntity = new CommentEntity();
        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);
        when(modelMapper.map(commentDto, CommentEntity.class)).thenReturn(commentEntity);

        CommentEntity savedCommentEntity = new CommentEntity();
        when(commentRepository.save(commentEntity)).thenReturn(savedCommentEntity);
        when(commentService.create(commentDto)).thenReturn(commentDto);
        CommentDto createdCommentDto = commentService.create(commentDto);

        assertNotNull(createdCommentDto);
    }

    @Test
    public void testUpdateCommentNotFound() {
        CommentDto commentDto = new CommentDto();
        long commentId = 123;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(ArtExhibitException.class, () -> commentService.update(commentDto, commentId));
    }

    @Test
    public void testFindByNotificationIdNotificationNotFound() {
        long notificationId = 123;
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        assertThrows(ArtExhibitException.class, () -> commentService.findByNotificationId(notificationId, 0, 10));
    }

    @Test
    public void testFindByNotificationIdNotificationFound() {
        long notificationId = 1;

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(1L);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notificationEntity));

        Pageable paging = PageRequest.of(0, 10);
        Page<CommentEntity> commentEntities = mock(Page.class);
        when(commentRepository.findByNotification(notificationEntity, paging)).thenReturn(commentEntities);
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1L);
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        when(modelMapper.map(commentEntity, CommentDto.class)).thenReturn(commentDto);

        List<CommentDto> commentDtos = commentService.findByNotificationId(notificationId, 0, 10);

        assertNotNull(commentDtos);
    }

    @Test
    public void testDeleteCommentNotFound() {
        long commentId = 123;

        // Mock the commentRepository to return an empty Optional
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Call the delete method and expect it to throw an exception
        assertThrows(ArtExhibitException.class, () -> commentService.delete(commentId));
    }

    @Test
    public void testDeleteCommentFound() {
        long commentId = 1;

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1L);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        when(commentService.delete(commentId)).thenReturn("Comment is deleted successfully!");
        String resultMessage = commentService.delete(commentId);

        assertNotNull(resultMessage);
    }

    @Test
    public void testUpdateCommentFound() {
        CommentDto commentDto = new CommentDto();
        long commentId = 1;
        commentDto.setId(1L);
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setTitle("test");
        commentEntity.setId(1L);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));

        CommentEntity updatedCommentEntity = new CommentEntity();
        updatedCommentEntity.setId(1L);
        updatedCommentEntity.setTitle("testUpdated");
        when(modelMapper.map(commentDto, CommentEntity.class)).thenReturn(updatedCommentEntity);

        when(commentRepository.save(updatedCommentEntity)).thenReturn(updatedCommentEntity);

        when(commentService.update(commentDto, commentId)).thenReturn(commentDto);

        CommentDto updatedCommentDto = commentService.update(commentDto, commentId);
        assertNotNull(updatedCommentDto);
        assertEquals(updatedCommentEntity.getId(), updatedCommentDto.getId());
    }
}
