package com.example.restdocs.post.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostWriteRequest {

    @NotBlank(message = "제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "작성자를 입력해 주세요.")
    private String writer;

    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

}
