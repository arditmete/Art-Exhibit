package com.edu.artexhibit.controller;

import com.edu.artexhibit.service.impl.ArtCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collectors")
@RequiredArgsConstructor
public class ArtCollectorController {
    private final ArtCollectorService artCollectorService;

    @PostMapping("/add/{portfolioId}")
    public ResponseEntity<?> create(@PathVariable(name = "portfolioId") long portfolioId) {
        return ResponseEntity.ok(artCollectorService.add(portfolioId));
    }

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok((artCollectorService.get()));
    }

    @DeleteMapping("/delete/{artCollectorId}")
    public ResponseEntity<?> delete(@PathVariable(name = "artCollectorId") long artCollectorId) {
        return ResponseEntity.ok(artCollectorService.delete(artCollectorId));
    }
}
