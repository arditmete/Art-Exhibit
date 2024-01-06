package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.models.dtos.FileDto;
import com.edu.artexhibit.models.dtos.PortfolioDto;
import com.edu.artexhibit.models.entity.FileEntity;
import com.edu.artexhibit.models.entity.PortfolioEntity;
import com.edu.artexhibit.repository.CategoryRepository;
import com.edu.artexhibit.repository.FileRepository;
import com.edu.artexhibit.repository.NotificationRepository;
import com.edu.artexhibit.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.models.dtos.ArtistDto;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@SpringBootTest
public class PortfolioServiceTest {
    @Spy
    @InjectMocks
    private ArtistService artistService;

    @InjectMocks
    private NotificationService notificationService;
    @Mock
    private NotificationRepository notificationRepository;
    @InjectMocks
    private PortfolioService portfolioService;
    @Mock
    private PortfolioRepository portfolioRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private MessageProperties messageProperties;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        portfolioService = new PortfolioService(portfolioRepository,fileRepository,artistService, modelMapper,messageProperties, categoryRepository);
    }
    @Test
    public void testCreatePortfolio() {
        // Create a sample ArtistDto and PortfolioDto
        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(5L);
        PortfolioDto portfolioDto = new PortfolioDto();
        portfolioDto.setName("test");
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
        when(artistService.getLoggedInUser()).thenReturn(artistDto);

        PortfolioEntity portfolioEntity = new PortfolioEntity();
        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);
        when(modelMapper.map(portfolioDto, PortfolioEntity.class)).thenReturn(portfolioEntity);

        PortfolioEntity savedPortfolioEntity = new PortfolioEntity();
        when(portfolioRepository.save(portfolioEntity)).thenReturn(savedPortfolioEntity);

        List<FileDto> fileDtos = new ArrayList<>();
        fileDtos.add(new FileDto());
        portfolioDto.setFiles(fileDtos);

        List<FileEntity> fileEntities = new ArrayList<>();
        for (FileDto fileDto : fileDtos) {
            FileEntity fileEntity = new FileEntity();
            when(modelMapper.map(fileDto, FileEntity.class)).thenReturn(fileEntity);
            when(fileRepository.save(fileEntity)).thenReturn(fileEntity);
            fileEntities.add(fileEntity);
        }

        when(portfolioService.create(portfolioDto)).thenReturn(portfolioDto);
        // Call the create method
        PortfolioDto createdPortfolioDto = portfolioService.create(portfolioDto);

        assertNotNull(createdPortfolioDto);
    }
}
