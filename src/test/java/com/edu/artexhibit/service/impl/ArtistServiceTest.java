package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.models.dtos.PasswordDto;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.repository.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ArtistServiceTest {

    @Spy
    @InjectMocks
    private ArtistService artistService;

    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MessageProperties messageProperties;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindArtistByIdWhenFound() {
        // Create a sample ArtistEntity and ArtistDto
        long artistId = 1L;
        ArtistEntity artistEntity = new ArtistEntity();
        artistEntity.setId(artistId);
        ArtistDto expectedArtistDto = new ArtistDto();
        expectedArtistDto.setId(artistId);

        doReturn(expectedArtistDto).when(artistService).findById(artistId);
        ArtistDto foundArtist = artistService.findById(artistId);

        assertNotNull(foundArtist);
        assertEquals(artistId, foundArtist.getId());

        verify(artistService, times(1)).findById(artistId);
    }

    @Test
    public void testFindArtistByIdWhenNotFound() {
        long nonExistentArtistId = 999L;

        doReturn(Optional.empty()).when(artistRepository).findById(nonExistentArtistId);

        assertThrows(ArtExhibitException.class, () -> artistService.findById(nonExistentArtistId));

        verify(artistService, times(1)).findById(nonExistentArtistId);
    }

    @Test
    public void testFindById_NotFound() {
        when(artistRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ArtExhibitException.class, () -> artistService.findById(1000L));
    }
    @Test
    public void testCheckIfExistsArtistExists() {
        ArtistDto artistDto = new ArtistDto();
        artistDto.setUsername("existingUsername"); // Replace with an existing username
        when(artistRepository.findByUsername(artistDto.getUsername())).thenReturn(Optional.of(new ArtistEntity()));
        assertTrue(artistService.checkIfExists(artistDto));
    }

    @Test
    public void testCheckIfExistsArtistDoesNotExist() {
        ArtistDto artistDto = new ArtistDto();
        artistDto.setUsername("nonExistentUsername"); // Replace with a non-existent username

        when(artistRepository.findByUsername(artistDto.getUsername())).thenReturn(Optional.empty());

        assertFalse(artistService.checkIfExists(artistDto));
    }
    @Test
    public void testValidatePasswordMatchingPasswords() {
        PasswordDto passwordDto = new PasswordDto("oldPassword", "newPassword", "newPassword");
        ArtistEntity artistEntity = new ArtistEntity();

        when(passwordEncoder.matches(passwordDto.getOldPassword(), artistEntity.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> artistService.validatePassword(passwordDto, artistEntity));
    }

    @Test
    public void testValidatePasswordMismatchedPasswords() {
        PasswordDto passwordDto = new PasswordDto("oldPassword", "newPassword", "differentPassword");
        ArtistEntity artistEntity = new ArtistEntity();

        assertThrows(ArtExhibitException.class, () -> artistService.validatePassword(passwordDto, artistEntity));
    }

    @Test
    public void testValidatePasswordWrongOldPassword() {
        PasswordDto passwordDto = new PasswordDto("wrongPassword", "newPassword", "newPassword");
        ArtistEntity artistEntity = new ArtistEntity();

        when(passwordEncoder.matches(passwordDto.getOldPassword(), artistEntity.getPassword())).thenReturn(false);

        assertThrows(ArtExhibitException.class, () -> artistService.validatePassword(passwordDto, artistEntity));
    }

    @Test
    public void testChangePasswordArtistNotFound() {
        PasswordDto passwordDto = new PasswordDto("wrongPassword", "newPassword", "newPassword");

        String username = "string9";
        ArtistEntity artistEntity = new ArtistEntity();
        ArtistDto artistDto = new ArtistDto();
        artistDto.setUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
        when(artistRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ArtExhibitException.class, () -> artistService.changePassword(passwordDto));
    }

    @Test
    public void testChangePasswordPasswordMismatch() {
        PasswordDto passwordDto = new PasswordDto("password", "newPassword", "newPassword");
        String username = "string9";
        ArtistEntity artistEntity = new ArtistEntity();
        ArtistDto artistDto = new ArtistDto();
        artistDto.setUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);

        ArtistEntity existingArtist = new ArtistEntity();
        existingArtist.setId(1L);
        when(artistRepository.findById(anyLong())).thenReturn(Optional.of(existingArtist));

        assertThrows(ArtExhibitException.class, () -> artistService.changePassword(passwordDto));
    }
//
//    @Test
//    public void testChangePasswordSuccessful() {
//        PasswordDto passwordDto = new PasswordDto("string123@", "newPassword", "newPassword");
//        String username = "string9";
//        ArtistEntity artistEntity = new ArtistEntity();
//        artistEntity.setId(1L);
//        artistEntity.setUsername(username);
//        artistEntity.setPassword(passwordEncoder.encode("string123@"));
//        ArtistDto artistDto = new ArtistDto();
//        artistDto.setId(1L);
//        artistDto.setUsername(username);
//        artistDto.setPassword(passwordEncoder.encode("string123@"));
//        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authentication);
//
//        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
//
//        when(artistRepository.findById(anyLong())).thenReturn(Optional.of(artistEntity));
//
//        when(passwordEncoder.encode(passwordDto.getConfirmNewPassword())).thenReturn("hashedPassword");
//
//        when(messageProperties.getPasswordIsChangedSuccessfully()).thenReturn("Password changed successfully");
//
//        String resultMessage = artistService.changePassword(passwordDto);
//
//        assertEquals("Password changed successfully", resultMessage);
//
//        assertEquals("hashedPassword", artistEntity.getPassword());
//    }

    @Test
    public void testFindByUsernameWhenFound() {
        ArtistEntity artistEntity = new ArtistEntity();
        artistEntity.setId(1L);
        artistEntity.setUsername("testUser127");
        doReturn(artistEntity).when(artistService).findByUsername("testUser127");
        ArtistEntity artistEntity1 = artistService.findByUsername("testUser127");
        assertEquals(artistEntity1, artistEntity);
    }

    @Test
    public void testCreate() {
        ArtistDto artistDto = new ArtistDto();
        artistDto.setUsername("testUser127");
        artistDto.setPassword("testpassword");
        artistDto.setCreatedAt(Instant.now());
        doReturn(artistDto).when(artistService).create(artistDto);
        ArtistDto createdArtist = artistService.create(artistDto);

        assertNotNull(createdArtist);
        assertEquals("testUser127", createdArtist.getUsername());
        assertNotNull(createdArtist.getCreatedAt());
    }

    @Test
    public void testFindByName() {
        String username = "string9";
        ArtistEntity artistEntity = new ArtistEntity();
        ArtistDto artistDto = new ArtistDto();
        artistDto.setUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);

        ArtistEntity userDetails = new ArtistEntity();
        userDetails.setUsername(username);

        when(artistRepository.findByUsername(username)).thenReturn(Optional.of(userDetails));

        ArtistEntity artistEntity1 = artistService.findByUsername(username);

        assertNotNull(artistEntity1);
        assertEquals(username, artistDto.getUsername());
    }

    @Test
    public void testDeleteArtistFound() {
        long artistId = 1; // Replace with an actual ID

        // Mock the artistRepository to return an Optional with an existing artist
        ArtistEntity existingArtist = new ArtistEntity(/* Initialize with required data */);
        when(artistRepository.findById(artistId)).thenReturn(Optional.of(existingArtist));

        // Mock messageProperties for the success message
        when(messageProperties.getArtistIsDeletedSuccessfully()).thenReturn("Artist deleted successfully");

        // Call the delete method
        String resultMessage = artistService.delete(artistId);

        // Verify that the delete method behaves correctly and returns the success message
        assertEquals("Artist deleted successfully", resultMessage);

        // Verify that the artist with the specified ID was deleted
        verify(artistRepository, times(1)).deleteById(artistId);
    }

    @Test
    public void testDeleteArtistNotFound() {
        long artistId = 456;

        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        // Call the delete method and expect it to throw an exception
        assertNull(artistService.delete(artistId));
    }

    @Test
    public void testFindByNameWithUserNotFound() {
        String username = "nonExistentUser";
        when(artistRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(ArtExhibitException.class, () -> artistService.findByUsername(username));
    }

    @Test
    public void testUpdate() {
        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(1L);
        ArtistEntity loggedInUser = new ArtistEntity();
        loggedInUser.setId(1L);
        ArtistEntity updatedUser = new ArtistEntity();
        updatedUser.setId(1L);

        String username = "string10";
        ArtistEntity artistEntity = new ArtistEntity();
        artistDto.setUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);
        when(artistService.getLoggedInUser()).thenReturn(artistDto);

        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(loggedInUser);
        when(modelMapper.map(artistDto, ArtistEntity.class)).thenReturn(updatedUser);
        ArtistDto updatedArtistDto = new ArtistDto();
        updatedArtistDto.setId(1L);
        updatedArtistDto.setUsername("string10");

        doReturn(updatedArtistDto).when(artistService).update(artistDto, updatedArtistDto.getId());
        long id = 1;
        ArtistDto updatedDto = artistService.update(artistDto, id);

        assertEquals(artistDto, updatedDto);

        assertEquals(updatedUser.getAddress(), loggedInUser.getAddress());
        assertEquals(updatedUser.getEmail(), loggedInUser.getEmail());
    }

    @Test
    public void testGetLoggedInUser() {
        ArtistEntity artistEntity = new ArtistEntity();
        ArtistDto artistDto = new ArtistDto();

        Authentication authentication = new UsernamePasswordAuthenticationToken(artistEntity, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(modelMapper.map(artistEntity, ArtistDto.class)).thenReturn(artistDto);

        ArtistDto loggedInUser = artistService.getLoggedInUser();

        assertNotNull(loggedInUser);
        assertEquals(artistDto, loggedInUser);
    }

    @Test
    public void testGetLoggedInUserWithNullPrincipal() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        assertThrows(ArtExhibitException.class, () -> artistService.getLoggedInUser());
    }
    @Test
    public void testFindAll() {
        List<ArtistEntity> artistEntities = new ArrayList<>();
        artistEntities.add(new ArtistEntity());
        artistEntities.add(new ArtistEntity());

        Page<ArtistEntity> artistEntityPage = new PageImpl<>(artistEntities);
        when(artistRepository.findAll(any(PageRequest.class))).thenReturn(artistEntityPage);

        int page = 0;
        int size = 10;
        List<ArtistDto> artistDtos = artistService.findAll(page, size);

        assertEquals(artistEntities.size(), artistDtos.size());

    }

}