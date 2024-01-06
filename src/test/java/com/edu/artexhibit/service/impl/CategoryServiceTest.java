package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.models.dtos.CategoryDto;
import com.edu.artexhibit.models.dtos.PortfolioDto;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.models.entity.CategoryEntity;
import com.edu.artexhibit.repository.CategoryRepository;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.exceptions.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ArtistService artistService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PortfolioService portfolioService;

    @Mock
    private MessageProperties messageProperties;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepository, artistService, modelMapper, portfolioService, messageProperties);
    }

    @Test
    public void testFindByIdCategoryFound() {
        // Arrange
        long categoryId = 1;
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(5L);
        String username = "string9";
        ArtistEntity artistEntity = new ArtistEntity();
        artistDto.setUsername(username);
        artistEntity.setId(5L);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);
        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);
        when(categoryRepository.findByIdAndArtist(categoryId, artistEntity)).thenReturn(Optional.of(categoryEntity));
        when(modelMapper.map(categoryEntity, CategoryDto.class)).thenReturn(categoryDto);
        when(categoryService.findById(categoryId)).thenReturn(categoryDto);
        // Act
        CategoryDto result = categoryService.findById(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(categoryId, result.getId());
    }

//    @Test
//    public void testFindByIdCategoryNotFound() {
//        // Arrange
//        long categoryId = 1;
//        ArtistDto artistDto = new ArtistDto();
//        artistDto.setId(5L);
//        String username = "string9";
//        ArtistEntity artistEntity = new ArtistEntity();
//        artistDto.setUsername(username);
//        artistEntity.setId(5L);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authentication);
//
//        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
//        when(artistService.getLoggedInUser()).thenReturn(artistDto);
//        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);
//        doReturn(artistDto).when(artistService);
//        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(new ArtistEntity());
//        when(categoryRepository.findByIdAndArtist(categoryId, new ArtistEntity())).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertThrows(ArtExhibitException.class, () -> categoryService.findById(categoryId));
//    }

//    @Test
//    public void testFindAll() {
//        // Arrange
//        int page = 0;
//        int size = 10;
//        ArtistDto artistDto = new ArtistDto();
//        artistDto.setId(1L);
//        Pageable pageable = PageRequest.of(page, size);
//        Page<CategoryEntity> categoryEntities = mock(Page.class);
//        artistDto.setId(5L);
//        String username = "string9";
//        ArtistEntity artistEntity = new ArtistEntity();
//        artistDto.setUsername(username);
//        artistEntity.setId(5L);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authentication);
//
//        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
//        when(artistService.getLoggedInUser()).thenReturn(artistDto);
//        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);
//        when(artistService.getLoggedInUser()).thenReturn(artistDto);
//        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(new ArtistEntity());
//        when(categoryRepository.findByArtist(new ArtistEntity(), pageable)).thenReturn(categoryEntities);
////        when(modelMapper.map(categoryEntities, CategoryDto.class)).thenReturn(new ArrayList<>());
//
//        // Act
//        List<CategoryDto> result = categoryService.findAll(page, size);
//
//        // Assert
//        assertNotNull(result);
//    }

    @Test
    public void testFindByNameCategoryFound() {
        // Arrange
        String categoryName = "TestCategory";
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryName);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(categoryName);

        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(categoryEntity));
        when(modelMapper.map(categoryEntity, CategoryDto.class)).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.findByName(categoryName);

        // Assert
        assertNotNull(result);
        assertEquals(categoryName, result.getName());
    }

    @Test
    public void testFindByNameCategoryNotFound() {
        // Arrange
        String categoryName = "NonExistentCategory";

        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ArtExhibitException.class, () -> categoryService.findByName(categoryName));
    }

    @Test
    public void testUpdateCategoryFound() {
        // Arrange
        long categoryId = 1;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("UpdatedCategory");
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);
        categoryEntity.setName("OldCategory");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);
        when(modelMapper.map(categoryEntity, CategoryDto.class)).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.update(categoryDto, categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(categoryDto.getName(), result.getName());
    }

    @Test
    public void testUpdateCategoryNotFound() {
        // Arrange
        long categoryId = 1;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("UpdatedCategory");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ArtExhibitException.class, () -> categoryService.update(categoryDto, categoryId));
    }
//
//    @Test
//    public void testDeleteCategoryFound() {
//        // Arrange
//        long categoryId = 1;
//        CategoryEntity categoryEntity = new CategoryEntity();
//        categoryEntity.setId(1L);
//        CategoryDto categoryDto = new CategoryDto();
//        categoryDto.setId(categoryId);
//        ArtistDto artistDto = new ArtistDto();
//        artistDto.setId(5L);
//        String username = "string9";
//        ArtistEntity artistEntity = new ArtistEntity();
//        artistDto.setUsername(username);
//        artistEntity.setId(5L);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authentication);
//
//        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
//        when(artistService.getLoggedInUser()).thenReturn(artistDto);
//        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);
//        when(modelMapper.map(categoryDto, CategoryEntity.class)).thenReturn(new CategoryEntity());
//        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
//        when(categoryService.findById(categoryId)).thenReturn(categoryDto);
//        when(categoryService.delete(categoryId)).thenReturn(messageProperties.getCategoryIsDeletedSuccessfully());
//        // Act
//        String result = categoryService.delete(categoryId);
//
//        // Assert
//        assertNotNull(result);
//    }

//    @Test
//    public void testDeleteCategoryNotFound() {
//        // Arrange
//        long categoryId = 1000;
//        String username = "string9";
//        ArtistDto artistDto = new ArtistDto();
//        artistDto.setId(5L);
//        artistDto.setUsername(username);
//        CategoryEntity categoryEntity = new CategoryEntity();
//        categoryEntity.setId(categoryId);
//        ArtistEntity artistEntity = new ArtistEntity();
//        artistEntity.setId(5L);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authentication);
//        when(artistService.getLoggedInUser()).thenReturn(artistDto);
//        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(artistEntity);
//        when(categoryRepository.findByIdAndArtist(categoryId, artistEntity)).thenReturn(Optional.empty());
//        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
//        when(categoryService.findById(categoryId)).thenThrow(new ArtExhibitException(ErrorCode.CATEGORY_NOT_FOUND));
//        when(categoryService.delete(categoryId)).thenThrow(new ArtExhibitException(ErrorCode.CATEGORY_NOT_FOUND));
//        assertThrows(ArtExhibitException.class, ()-> categoryService.delete(categoryId));
//    }

//    @Test
//    public void testCreateCategoryNotExists() {
//        // Arrange
//        CategoryDto categoryDto = new CategoryDto();
//        categoryDto.setName("NewCategory");
//        categoryDto.setPortfolio(new PortfolioDto());
//        ArtistDto artistDto = new ArtistDto();
//        artistDto.setId(5L);
//        String username = "string9";
//        ArtistEntity artistEntity = new ArtistEntity();
//        artistDto.setUsername(username);
//        artistEntity.setId(5L);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authentication);
//
//        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
//        when(artistService.getLoggedInUser()).thenReturn(artistDto);
//        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(new ArtistEntity());
//        when(modelMapper.map(categoryDto, CategoryEntity.class)).thenReturn(new CategoryEntity());
//        when(categoryRepository.findByName(categoryDto.getName())).thenReturn(Optional.empty());
//        when(portfolioService.findById(categoryDto.getPortfolio().getId())).thenReturn(new PortfolioDto());
//
//        // Act
//        CategoryDto result = categoryService.create(categoryDto);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(categoryDto.getName(), result.getName());
//    }

    @Test
    public void testCreateCategoryExists() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("ExistingCategory");

        when(categoryRepository.findByName(categoryDto.getName())).thenReturn(Optional.of(new CategoryEntity()));

        // Act and Assert
        assertThrows(ArtExhibitException.class, () -> categoryService.create(categoryDto));
    }

    @Test
    public void testCheckIfExistsCategoryExists() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("ExistingCategory");

        when(categoryRepository.findByName(categoryDto.getName())).thenReturn(Optional.of(new CategoryEntity()));

        // Act
        boolean result = categoryService.checkIfExists(categoryDto);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCheckIfExistsCategoryNotExists() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("NonExistentCategory");

        when(categoryRepository.findByName(categoryDto.getName())).thenReturn(Optional.empty());

        // Act
        boolean result = categoryService.checkIfExists(categoryDto);

        // Assert
        assertFalse(result);
    }
}
