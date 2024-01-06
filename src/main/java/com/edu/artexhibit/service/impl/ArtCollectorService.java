package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.exceptions.ErrorCode;
import com.edu.artexhibit.models.dtos.ArtCollectorDto;
import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.models.entity.ArtCollectorEntity;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.models.entity.PortfolioEntity;
import com.edu.artexhibit.models.enums.ArtistCategory;
import com.edu.artexhibit.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.edu.artexhibit.repository.ArtCollectorRepository;

@Service
@RequiredArgsConstructor
public class ArtCollectorService {
    private final ArtCollectorRepository artCollectorRepository;
    private final PortfolioRepository portfolioRepository;
    private final ArtistService artistService;
    private final ModelMapper modelMapper;
    private final MessageProperties messageProperties;

    public ArtCollectorDto add(long portfolioId) {
        ArtistDto artist = artistService.getLoggedInUser();
        if(!artist.getCategory().equals(ArtistCategory.ART_COLLECTOR)){
            throw new ArtExhibitException(ErrorCode.ARTIST_NOT_ALLOWED_FOR_COLLECTIONS);
        }
        ArtistEntity artistEntity = modelMapper.map(artist, ArtistEntity.class);
        if(!isPortfolioPresent(portfolioId, artistEntity.getId())) {
            Optional<PortfolioEntity> portfolioEntity = portfolioRepository.findById(portfolioId);
            if (!portfolioEntity.isPresent()) {
                throw new ArtExhibitException(ErrorCode.PORTFOLIO_NOT_FOUND);
            }
            ArtCollectorEntity artCollector = new ArtCollectorEntity();
            artCollector.setPortfolio(portfolioEntity.get());
            artCollector.setArtist(artistEntity);
            artCollector.setCreator(portfolioEntity.get().getArtist());
            return modelMapper.map(artCollectorRepository.save(artCollector), ArtCollectorDto.class);
        }
        throw new ArtExhibitException(ErrorCode.PORTFOLIO_ALREADY_REGISTERED);
    }

    public List<ArtCollectorEntity> get(){
        ArtistDto artist = artistService.getLoggedInUser();
        if(!artist.getCategory().equals(ArtistCategory.ART_COLLECTOR)){
            throw new ArtExhibitException(ErrorCode.ARTIST_NOT_ALLOWED_FOR_COLLECTIONS);
        }
        ArtistEntity artistEntity = modelMapper.map(artist, ArtistEntity.class);
        List<ArtCollectorEntity> artCollectorEntities = artCollectorRepository.findAllByArtistId(artistEntity.getId());
        if(artCollectorEntities.isEmpty()){
            throw new ArtExhibitException(ErrorCode.ARTIST_COLLECTIONS_NOT_FOUND);
        }
        return artCollectorEntities;
    }

    public String delete(long artCollectorId){
        artCollectorRepository.deleteById(artCollectorId);
        return messageProperties.getArtCollectorIsDeletedSuccessfully();
    }

    public boolean isPortfolioPresent(long portfolioId, long artistId){
        return artCollectorRepository.findByPortfolioIdAndArtistId(portfolioId, artistId).isPresent();
    }
}
