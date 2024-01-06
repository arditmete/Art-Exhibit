package com.edu.artexhibit.service;

import com.edu.artexhibit.models.dtos.AuthRequestDto;
import com.edu.artexhibit.models.dtos.AuthResponseDto;
import com.edu.artexhibit.config.JwtUtils;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.exceptions.ErrorCode;
import com.edu.artexhibit.service.impl.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    public final ArtistService artistService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthResponseDto authenticate(AuthRequestDto request) {
       try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           request.getUsername(),
                           request.getPassword()
                   )
           );
       }catch (Exception e){
          throw new ArtExhibitException(ErrorCode.USERNAME_OR_PASSWORD_INCORRECT);
       }
        ArtistEntity user = artistService.findByUsername(request.getUsername());
        String jwtToken = jwtUtils.generateToken(user);
        return AuthResponseDto.builder()
                .accessToken(jwtToken)
                .username(user.getUsername())
                .profileImage(user.getProfileImage())
                .category(user.getArtistCategory())
                .build();
    }
}
