package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.models.dtos.*;
import com.edu.artexhibit.models.entity.*;
import com.edu.artexhibit.repository.CategoryRepository;
import com.edu.artexhibit.repository.FileRepository;
import com.edu.artexhibit.repository.PortfolioRepository;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.exceptions.ErrorCode;
import com.edu.artexhibit.service.IEntityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService implements IEntityService<PortfolioDto> {

    private final PortfolioRepository portfolioRepository;
    private final FileRepository fileRepository;
    private final ArtistService artistService;
    private final ModelMapper modelMapper;
    private final MessageProperties messageProperties;
    private final CategoryRepository categoryRepository;

    //will find the portfolio of logged in artist
    @Override
    public PortfolioDto findById(long id) {
        ArtistDto artist = artistService.getLoggedInUser();
        ArtistEntity artistEntity = modelMapper.map(artist, ArtistEntity.class);
        Optional<PortfolioEntity> portfolioEntity = portfolioRepository.findByArtist(artistEntity);
        if (portfolioEntity.isPresent()) {
            PortfolioDto portfolioDtos = modelMapper.map(portfolioEntity, PortfolioDto.class);
            portfolioDtos.setCategoryDtos(getCategoriesByPortfolio(artistEntity));
            portfolioDtos.getArtist().setPortfolio(null);
            return portfolioDtos;
        } else {
            throw new ArtExhibitException(ErrorCode.PORTFOLIO_NOT_FOUND);
        }
    }

    public PortfolioDto findByArtistId(long id) {
        ArtistDto artistDto = artistService.findById(id);
        ArtistEntity artistEntity = modelMapper.map(artistDto, ArtistEntity.class);
        Optional<PortfolioEntity> portfolioEntity = portfolioRepository.findByArtist(artistEntity);
        if (portfolioEntity.isPresent()) {
            PortfolioDto portfolioDtos = modelMapper.map(portfolioEntity, PortfolioDto.class);
            portfolioDtos.setCategoryDtos(getCategoriesByPortfolio(artistEntity));
            return portfolioDtos;
        } else {
            throw new ArtExhibitException(ErrorCode.PORTFOLIO_NOT_FOUND);
        }
    }

    @Override
    public PortfolioDto findByName(String name) {
        PortfolioEntity portfolioEntity = portfolioRepository.findByName(name).orElseThrow(()->new ArtExhibitException(ErrorCode.PORTFOLIO_NOT_FOUND));
        return modelMapper.map(portfolioEntity, PortfolioDto.class);
    }

    @Override
    public PortfolioDto update(PortfolioDto portfolioDto, long id) {
        ArtistDto artist = artistService.getLoggedInUser();
        ArtistEntity artistEntity = modelMapper.map(artist, ArtistEntity.class);
        PortfolioEntity portfolioEntity = portfolioRepository.findByArtist(artistEntity).orElseThrow(()->new ArtExhibitException(ErrorCode.PORTFOLIO_NOT_FOUND));
        portfolioEntity.setUpdatedAt(Instant.now());
        portfolioEntity.setName(portfolioDto.getName());
        if(portfolioDto.getJsonTheme() != null) {
            portfolioEntity.setJsonTheme(portfolioDto.getJsonTheme());
        }
        if(portfolioDto.getFiles() != null) {
            List<FileEntity> fileEntities = portfolioDto.getFiles().stream().map(fileDto -> modelMapper.map(fileDto, FileEntity.class)).collect(Collectors.toList());
            portfolioEntity.setFiles(fileEntities);
        }
        portfolioEntity = portfolioRepository.save(portfolioEntity);
        return modelMapper.map(portfolioEntity, PortfolioDto.class);
    }

    @Override
    @Transactional
    public String delete(long id) {
        ArtistDto artist = artistService.getLoggedInUser();
        ArtistEntity artistEntity = modelMapper.map(artist, ArtistEntity.class);
        portfolioRepository.deleteByIdAndArtist(id, artistEntity);
        return messageProperties.getPortfolioIsDeletedSuccessfully();
    }

    @Override
    public PortfolioDto create(PortfolioDto portfolioDto) {
        ArtistDto artistDto = artistService.getLoggedInUser();
        isPortfolioCreated(modelMapper.map(artistDto, ArtistEntity.class));

        ArtistEntity artistEntity = modelMapper.map(artistDto, ArtistEntity.class);
        PortfolioEntity portfolioEntity = modelMapper.map(portfolioDto, PortfolioEntity.class);
        portfolioEntity.setArtist(artistEntity);
        portfolioEntity.setCreatedAt(Instant.now());
        portfolioEntity.setJsonTheme(portfolioDto.getJsonTheme());
        PortfolioEntity newPortfolio = portfolioRepository.save(portfolioEntity);
        if(portfolioDto.getFiles() != null && !portfolioDto.getFiles().isEmpty()) {
            List<FileEntity> fileEntities = new ArrayList<>();
            for (FileDto fileDto : portfolioDto.getFiles()) {
                FileEntity fileEntity = modelMapper.map(fileDto, FileEntity.class);
                fileEntity.setCreatedAt(Instant.now());
                fileEntity.setPortfolio(newPortfolio);
                fileEntities.add(fileRepository.save(fileEntity));
            }
            newPortfolio.setFiles(fileEntities);
        }
        return modelMapper.map(portfolioEntity, PortfolioDto.class);
    }

    @Override
    public List<PortfolioDto> findAll(int page, int size) {
//        ArtistDto artist = artistService.getLoggedInUser();
//        ArtistEntity artistEntity = modelMapper.map(artist, ArtistEntity.class);
//        PortfolioEntity portfolioEntity = portfolioRepository.findByArtist(artistEntity);
//        if (portfolioEntity != null) {
//            List<PortfolioDto> portfolioDtos = Collections.singletonList(modelMapper.map(portfolioEntity, PortfolioDto.class));
//            //this will get 0 always as it will be only one portfolio at all for one artist
//            portfolioDtos.get(0).setCategoryDtos(getCategoriesByPortfolio(artistEntity));
//            portfolioDtos.get(0).setEventDtos(getEventsByPortfolio(artistEntity));
//
//        return portfolioDtos;
//        } else {
//            return Collections.emptyList();
//        }
        return Collections.emptyList();
    }

    @Override
    public boolean checkIfExists(PortfolioDto portfolioDto) {
        Optional<PortfolioEntity> existingPortfolio = portfolioRepository.findByName(portfolioDto.getName());
        return existingPortfolio.isPresent();
    }

    private void isPortfolioCreated(ArtistEntity artistEntity){
        if(artistEntity.getPortfolio()!=null){
            throw new ArtExhibitException(ErrorCode.ONLY_ONE_PORTFOLIO_ALLOWED);
        }
    }

    private List<CategoryDto> getCategoriesByPortfolio(ArtistEntity artistEntity) {
        List<CategoryEntity> categoryEntities = categoryRepository.findByArtist(artistEntity);
        List<CategoryDto> categoryDtos = new ArrayList<>();
        categoryEntities.forEach(categoryEntity ->
                categoryDtos.add(modelMapper.map(categoryEntity, CategoryDto.class))
        );
        categoryDtos.forEach(categoryDto ->
                categoryDto.setPortfolio(null));
        return categoryDtos;
    }
}
