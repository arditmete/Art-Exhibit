package com.edu.artexhibit.controller;

import com.edu.artexhibit.models.dtos.PortfolioDto;
import com.edu.artexhibit.service.IEntityService;
import com.edu.artexhibit.service.impl.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/portfolio")
@RequiredArgsConstructor
public class PortfolioController {
    private final IEntityService<PortfolioDto> entityService;
    private final PortfolioService portfolioService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PortfolioDto request) {
        return ResponseEntity.ok(entityService.create(request));
    }

    @GetMapping("getByArtist/{artistId}")
    public ResponseEntity<?> getPortfolioByArtistId(@PathVariable long artistId) {
        return ResponseEntity.ok((portfolioService.findByArtistId(artistId)));
    }

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok((entityService.findById(0)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody PortfolioDto request, @PathVariable long id) {
        return ResponseEntity.ok((entityService.update(request, id)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.ok(entityService.delete(id));
    }
}

