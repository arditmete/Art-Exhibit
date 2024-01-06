package com.edu.artexhibit.controller;

import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.models.dtos.AuthRequestDto;
import com.edu.artexhibit.models.dtos.PasswordDto;
import com.edu.artexhibit.service.AuthService;
import com.edu.artexhibit.service.impl.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ArtistService artistService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthRequestDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody ArtistDto request) {
        return ResponseEntity.ok(artistService.create(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordDto request) {
        return ResponseEntity.ok(artistService.changePassword(request));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok(artistService.getLoggedInUser());
    }
}
