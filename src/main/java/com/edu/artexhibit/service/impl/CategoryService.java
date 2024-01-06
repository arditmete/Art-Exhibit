package com.edu.artexhibit.service.impl;

import com.edu.artexhibit.config.MessageProperties;
import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.models.dtos.CategoryDto;
import com.edu.artexhibit.models.dtos.PortfolioDto;
import com.edu.artexhibit.models.entity.ArtistEntity;
import com.edu.artexhibit.models.entity.CategoryEntity;
import com.edu.artexhibit.models.entity.PortfolioEntity;
import com.edu.artexhibit.repository.CategoryRepository;
import com.edu.artexhibit.exceptions.ArtExhibitException;
import com.edu.artexhibit.exceptions.ErrorCode;
import com.edu.artexhibit.service.IEntityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements IEntityService<CategoryDto> {

    private final CategoryRepository categoryRepository;
    private final ArtistService artistService;
    private final ModelMapper modelMapper;
    private final PortfolioService portfolioService;
    private final MessageProperties messageProperties;

    @Override
    public CategoryDto findById(long id) {
        ArtistDto artist = artistService.getLoggedInUser();
        ArtistEntity artistEntity = modelMapper.map(artist, ArtistEntity.class);
        Optional<CategoryEntity> categoryEntity = categoryRepository.findByIdAndArtist(id, artistEntity);
        if(categoryEntity.isPresent()){
            return modelMapper.map(categoryEntity, CategoryDto.class);
        }
        throw new ArtExhibitException(ErrorCode.CATEGORY_NOT_FOUND);
    }
    @Override
    public List<CategoryDto> findAll(int page, int size) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        ArtistDto artist = artistService.getLoggedInUser();
        ArtistEntity artistEntity = modelMapper.map(artist, ArtistEntity.class);
        Pageable paging = PageRequest.of(page,size);
        Page<CategoryEntity> categoryEntities = categoryRepository.findByArtist(artistEntity, paging);
        categoryEntities.forEach(
                categoryEntity -> categoryDtos.add(modelMapper.map(categoryEntity, CategoryDto.class))
        );
        return categoryDtos;
    }

    @Override
    public CategoryDto findByName(String name){
        CategoryEntity categoryEntity = categoryRepository.findByName(name).orElseThrow(()->new ArtExhibitException(ErrorCode.CATEGORY_NOT_FOUND));
        return modelMapper.map(categoryEntity, CategoryDto.class);
    }
    @Override
    public CategoryDto update(CategoryDto categoryDto, long id ) {
        CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(()->new ArtExhibitException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryEntity.setUpdatedAt(Instant.now());
        categoryEntity.setName(categoryDto.getName());
        categoryEntity = categoryRepository.save(categoryEntity);
        return modelMapper.map(categoryEntity, CategoryDto.class);
    }

    @Override
    public String delete(long id) {
        CategoryDto categoryDto = findById(id);
        CategoryEntity categoryEntity = modelMapper.map(categoryDto, CategoryEntity.class);
        categoryRepository.delete(categoryEntity);
        return messageProperties.getCategoryIsDeletedSuccessfully();
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        if (checkIfExists(categoryDto)) {
            throw new ArtExhibitException(ErrorCode.CATEGORY_ALREADY_REGISTERED);
        }
        ArtistDto artistDto = artistService.getLoggedInUser();
        ArtistEntity artistEntity = modelMapper.map(artistDto, ArtistEntity.class);
        PortfolioDto portfolioDto = portfolioService.findById(categoryDto.getPortfolio().getId());
        PortfolioEntity portfolioEntity = modelMapper.map(portfolioDto, PortfolioEntity.class);
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setArtist(artistEntity);
        categoryEntity.setCreatedAt(Instant.now());
        categoryEntity.setName(categoryDto.getName());
        categoryEntity.setPortfolio(portfolioEntity);
       categoryRepository.save(categoryEntity);
        return modelMapper.map(categoryEntity, CategoryDto.class);
    }

    @Override
    public boolean checkIfExists(CategoryDto categoryDto) {
        Optional<CategoryEntity> existingCategory = categoryRepository.findByName(categoryDto.getName());
        return existingCategory.isPresent();
    }
}
