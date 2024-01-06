package com.edu.artexhibit.controller;

import com.edu.artexhibit.models.dtos.CommentDto;
import com.edu.artexhibit.service.impl.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CommentDto request) {
        return ResponseEntity.ok(commentService.create(request));
    }
    @GetMapping("/findByNotificationId/{id}")
    public ResponseEntity<?> get(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "5") int size,
                                 @PathVariable long id) {
        return ResponseEntity.ok((commentService.findByNotificationId(id, page, size)));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody CommentDto commentDto, @PathVariable long id) {
        return ResponseEntity.ok((commentService.update(commentDto, id)));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.ok(commentService.delete(id));
    }
}

