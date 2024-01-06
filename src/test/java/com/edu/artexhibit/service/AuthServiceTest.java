package com.edu.artexhibit.service;

import com.edu.artexhibit.models.dtos.AuthRequestDto;
import com.edu.artexhibit.models.dtos.AuthResponseDto;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.config.JwtUtils;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.service.impl.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private AuthService authService;

    @Mock
    private ArtistService artistService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(artistService, authenticationManager, jwtUtils);
    }

    @Test
    public void testAuthenticateWithValidCredentials() {
        // Arrange
        AuthRequestDto request = new AuthRequestDto("username", "password");
        ArtistEntity mockUser = new ArtistEntity();
        mockUser.setUsername("username");
        when(artistService.findByUsername("username")).thenReturn(mockUser);
        when(jwtUtils.generateToken(mockUser)).thenReturn("mockToken");

        // Act
        AuthResponseDto response = authService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals("username", response.getUsername());
        assertEquals("mockToken", response.getAccessToken());
        // Add more assertions as needed
    }

    @Test
    public void testAuthenticateWithInvalidCredentials() {
        // Arrange
        AuthRequestDto request = new AuthRequestDto("invalidUsername", "invalidPassword");
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Authentication failed"));

        // Act and Assert
        assertThrows(ArtExhibitException.class, () -> authService.authenticate(request));
    }
}