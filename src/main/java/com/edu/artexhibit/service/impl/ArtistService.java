package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.models.dtos.PasswordDto;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.repository.ArtistRepository;
import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.service.IEntityService;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.exceptions.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistService implements IEntityService<ArtistDto> {

    private final ArtistRepository artistRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MessageProperties messageProperties;

    @Override
    public ArtistDto findById(long id) {
        Optional<ArtistEntity> artistEntity = artistRepository.findById(id);
        if (artistEntity.isPresent()) {
            return modelMapper.map(artistEntity, ArtistDto.class);
        }
        throw new ArtExhibitException(ErrorCode.USER_NOT_FOUND);
    }

    @Override
    public List<ArtistDto> findAll(int page, int size) {
        List<ArtistDto> artistDtos = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);
        Page<ArtistEntity> artistEntities = artistRepository.findAll(paging);
        artistEntities.forEach(
                artistEntity -> artistDtos.add(modelMapper.map(artistEntity, ArtistDto.class))
        );
        return artistDtos;
    }

    @Override
    public ArtistDto findByName(String username) throws UsernameNotFoundException {
        UserDetails userDetails = artistRepository.findByUsername(username).orElseThrow(() -> new ArtExhibitException(ErrorCode.USER_NOT_FOUND));
        return modelMapper.map(userDetails, ArtistDto.class);
    }

    @Override
    public ArtistDto update(ArtistDto artistDto, long id) {
        ArtistDto loggedInUser = getLoggedInUser();
        ArtistEntity currentUser = modelMapper.map(loggedInUser, ArtistEntity.class);
        ArtistEntity updatedUser = modelMapper.map(artistDto, ArtistEntity.class);
        currentUser.setAddress(updatedUser.getAddress());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setStudent(updatedUser.getStudent());
        currentUser.setProfileImage(updatedUser.getProfileImage());
        currentUser.setDescription(updatedUser.getDescription());
        currentUser.setBirthOfDate(updatedUser.getBirthOfDate());
        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setPhoneNumber(updatedUser.getPhoneNumber());
        currentUser.setUpdatedAt(Instant.now());
        currentUser.setAddress(updatedUser.getAddress());
        currentUser.setUsername(updatedUser.getUsername());
        currentUser.setPortfolio(updatedUser.getPortfolio());
        artistRepository.save(currentUser);
        return modelMapper.map(currentUser, ArtistDto.class);
    }

    @Override
    @Transactional
    public String delete(long id) {
        artistRepository.deleteById(id);
        return messageProperties.getArtistIsDeletedSuccessfully();
    }

    private void validateArtist(ArtistDto artistDto) {
        if (checkIfExists(artistDto)) {
            throw new ArtExhibitException(ErrorCode.USER_ALREADY_REGISTERED);
        }
    }

    @Override
    public ArtistDto create(ArtistDto artistDto) {
        validateArtist(artistDto);
        artistDto.setCreatedAt(Instant.now());
        artistDto.setPassword(passwordEncoder.encode(artistDto.getPassword()));
        ArtistEntity newArtist = artistRepository.save(modelMapper.map(artistDto, ArtistEntity.class));
        return modelMapper.map(newArtist, ArtistDto.class);
    }

    public String changePassword(PasswordDto passwordDto) {
       Optional<ArtistEntity> artistEntity = artistRepository.findById(getLoggedInUser().getId());
       if(!artistEntity.isPresent()) throw new ArtExhibitException(ErrorCode.ARTIST_NOT_FOUND);
       validatePassword(passwordDto, artistEntity.get());
       artistEntity.ifPresent(artist -> artist.setPassword(passwordEncoder.encode(passwordDto.getConfirmNewPassword())));
       artistRepository.save(artistEntity.get());
       return messageProperties.getPasswordIsChangedSuccessfully();
    }

     void validatePassword(PasswordDto passwordDto, ArtistEntity artist){
        if(!Objects.equals(passwordDto.getNewPassword(), passwordDto.getConfirmNewPassword()))
            throw new ArtExhibitException(ErrorCode.PASSWORDS_NOT_MATCHED);
        if(!passwordEncoder.matches(passwordDto.getOldPassword(), artist.getPassword()))
            throw new ArtExhibitException(ErrorCode.OLD_PASSWORD_WRONG);
    }

    @Override
    public boolean checkIfExists(ArtistDto artistDto) {
        Optional<ArtistEntity> existingUser = artistRepository.findByUsername(artistDto.getUsername());
        return existingUser.isPresent();
    }

    public ArtistEntity findByUsername(String username) throws UsernameNotFoundException {
        return artistRepository.findByUsername(username).orElseThrow(() -> new ArtExhibitException(ErrorCode.USER_NOT_FOUND));
    }

    public ArtistDto getLoggedInUser() {
        ArtistEntity artistEntity = (ArtistEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (artistEntity == null) throw new ArtExhibitException(ErrorCode.BAD_REQUEST, "Session Expired.");
        return modelMapper.map(artistEntity, ArtistDto.class);
    }
}