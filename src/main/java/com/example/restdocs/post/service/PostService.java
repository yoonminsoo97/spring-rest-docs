package com.example.restdocs.post.service;

import com.example.restdocs.post.dto.PostDetailResponse;
import com.example.restdocs.post.dto.PostWriteRequest;
import com.example.restdocs.post.entity.Post;
import com.example.restdocs.post.exception.NotFoundPostException;
import com.example.restdocs.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void postWrite(PostWriteRequest postWriteRequest) {
        Post post = Post.builder()
                .title(postWriteRequest.getTitle())
                .writer(postWriteRequest.getWriter())
                .content(postWriteRequest.getContent())
                .build();
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostDetailResponse postDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(NotFoundPostException::new);
        return new PostDetailResponse(post);
    }

}
