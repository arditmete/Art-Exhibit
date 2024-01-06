package com.edu.artexhibit.controller;

import com.edu.artexhibit.models.dtos.ArtistDto;
import com.edu.artexhibit.service.IEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/artist")
@RequiredArgsConstructor
public class ArtistController{

    private final IEntityService<ArtistDto> entityService;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable long id) {
        return ResponseEntity.ok((entityService.findById(id)));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(
            @Valid @RequestBody ArtistDto request) {
        return ResponseEntity.ok((entityService.update(request, 0)));
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> get(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok((entityService.findAll(page, size)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.ok(entityService.delete(id));
    }
}

