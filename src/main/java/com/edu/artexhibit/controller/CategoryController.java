package com.edu.artexhibit.controller;

import com.edu.artexhibit.models.dtos.CategoryDto;
import com.edu.artexhibit.service.IEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final IEntityService<CategoryDto> entityService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CategoryDto request) {
        return ResponseEntity.ok(entityService.create(request));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable long id) {
        return ResponseEntity.ok((entityService.findById(id)));
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> get(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok((entityService.findAll(page, size)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody CategoryDto request, @PathVariable long id) {
        return ResponseEntity.ok((entityService.update(request, id)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@Valid @PathVariable long id) {
        return ResponseEntity.ok(entityService.delete(id));
    }
}

