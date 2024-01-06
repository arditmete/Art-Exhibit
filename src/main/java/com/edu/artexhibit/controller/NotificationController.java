package com.edu.artexhibit.controller;

import com.edu.artexhibit.models.dtos.NotificationDto;
import com.edu.artexhibit.service.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid  @RequestBody NotificationDto request) {
        return ResponseEntity.ok(notificationService.create(request));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "5") int size,
                                 @PathVariable long id) {
        return ResponseEntity.ok((notificationService.findById(id, page, size)));
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> get(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok((notificationService.findAll(page, size)));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody NotificationDto notificationDto, @PathVariable long id) {
        return ResponseEntity.ok((notificationService.update(notificationDto, id)));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.ok(notificationService.delete(id));
    }
}

