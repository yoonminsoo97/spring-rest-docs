package com.example.restdocs.post.controller;

import com.example.restdocs.post.dto.PostDetailResponse;
import com.example.restdocs.post.dto.PostWriteRequest;
import com.example.restdocs.post.service.PostService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> postWrite(@Valid @RequestBody PostWriteRequest postWriteRequest) {
        postService.postWrite(postWriteRequest);
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> postDetail(@PathVariable("postId") Long postId) {
        PostDetailResponse postDetailResponse = postService.postDetail(postId);
        return ResponseEntity.ok().body(postDetailResponse);
    }

}
