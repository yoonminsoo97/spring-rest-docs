package com.example.restdocs.post;

import com.example.restdocs.config.RestDocsSupport;
import com.example.restdocs.post.controller.PostController;
import com.example.restdocs.post.dto.PostDetailResponse;
import com.example.restdocs.post.dto.PostWriteRequest;
import com.example.restdocs.post.exception.NotFoundPostException;
import com.example.restdocs.post.service.PostService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
public class PostControllerTest extends RestDocsSupport {

    @MockBean
    private PostService postService;

    @Nested
    @DisplayName("게시글 작성")
    class PostWrite {

        @Test
        @DisplayName("성공")
        void postWrite200OK() throws Exception {
            PostWriteRequest postWriteRequest = new PostWriteRequest("title", "writer", "content");

            willDoNothing().given(postService).postWrite(any(PostWriteRequest.class));

            mockMvc.perform(post("/api/posts")
                            .content(objectMapper.writeValueAsString(postWriteRequest))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("success"))
                    .andDo(restDocs.document(
                            PayloadDocumentation.requestFields(
                                    PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                    PayloadDocumentation.fieldWithPath("writer").type(JsonFieldType.STRING).description("게시글 작성자"),
                                    PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용")
                            )
                    ));

            then(postService).should().postWrite(any(PostWriteRequest.class));
        }

        @Test
        @DisplayName("잘못된 입력값")
        void postWrite400BadRequest() throws Exception {
            PostWriteRequest postWriteRequest = new PostWriteRequest("title", "", "content");

            mockMvc.perform(post("/api/posts")
                            .content(objectMapper.writeValueAsString(postWriteRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value("Bad Request"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.detail").value("입력값이 잘못되었습니다."))
                    .andExpect(jsonPath("$.instance").value("/api/posts"))
                    .andExpect(jsonPath("$.error_code").value("E400001"))
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors[0].field").value("writer"))
                    .andExpect(jsonPath("$.errors[0].input").value(""))
                    .andExpect(jsonPath("$.errors[0].message").value("작성자를 입력해 주세요."))
                    .andDo(restDocs.document(
                            PayloadDocumentation.requestFields(
                                    PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                    PayloadDocumentation.fieldWithPath("writer").type(JsonFieldType.STRING).description("게시글 작성자"),
                                    PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용")
                            ),
                            PayloadDocumentation.responseFields(
                                    PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("에러 요약"),
                                    PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                    PayloadDocumentation.fieldWithPath("detail").type(JsonFieldType.STRING).description("에러 설명"),
                                    PayloadDocumentation.fieldWithPath("instance").type(JsonFieldType.STRING).description("에러 발생 URI"),
                                    PayloadDocumentation.fieldWithPath("error_code").type(JsonFieldType.STRING).description("에러 코드"),
                                    PayloadDocumentation.fieldWithPath("errors").type(JsonFieldType.ARRAY).description("유효성 예외 목록"),
                                    PayloadDocumentation.fieldWithPath("errors[].field").type(JsonFieldType.STRING).description("필드명"),
                                    PayloadDocumentation.fieldWithPath("errors[].input").type(JsonFieldType.STRING).description("입력값"),
                                    PayloadDocumentation.fieldWithPath("errors[].message").type(JsonFieldType.STRING).description("필드 에러 설명")
                            )
                    ));

            then(postService).should(never()).postWrite(any(PostWriteRequest.class));
        }

    }

    @Nested
    @DisplayName("게시글 단건 조회")
    class PostDetail {

        @Test
        @DisplayName("성공")
        void postDetail200OK() throws Exception {
            PostDetailResponse postDetailResponse = new PostDetailResponse(1L, "title", "writer", "content");

            given(postService.postDetail(any(Long.class))).willReturn(postDetailResponse);

            mockMvc.perform(get("/api/posts/{postId}", 1))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.postId").value(1))
                    .andExpect(jsonPath("$.title").value("title"))
                    .andExpect(jsonPath("$.writer").value("writer"))
                    .andExpect(jsonPath("$.content").value("content"))
                    .andDo(restDocs.document(
                            RequestDocumentation.pathParameters(
                                    RequestDocumentation.parameterWithName("postId").description("게시글 번호")
                            ),
                            PayloadDocumentation.responseFields(
                                    PayloadDocumentation.fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 번호"),
                                    PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                    PayloadDocumentation.fieldWithPath("writer").type(JsonFieldType.STRING).description("게시글 작성자"),
                                    PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용")
                            )
                    ));

            then(postService).should().postDetail(any(Long.class));
        }

        @Test
        @DisplayName("존재하지 않는 게시글")
        void postDetail404NotFound() throws Exception {
            willThrow(new NotFoundPostException()).given(postService).postDetail(any(Long.class));

            mockMvc.perform(get("/api/posts/{postId}", 1))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title").value("Not Found"))
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.detail").value("게시글이 존재하지 않습니다."))
                    .andExpect(jsonPath("$.instance").value("/api/posts/1"))
                    .andExpect(jsonPath("$.error_code").value("E404001"))
                    .andDo(restDocs.document(
                            RequestDocumentation.pathParameters(
                                    RequestDocumentation.parameterWithName("postId").description("게시글 번호")
                            ),
                            PayloadDocumentation.responseFields(
                                    PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("에러 요약"),
                                    PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                    PayloadDocumentation.fieldWithPath("detail").type(JsonFieldType.STRING).description("에러 설명"),
                                    PayloadDocumentation.fieldWithPath("instance").type(JsonFieldType.STRING).description("에러 발생 URI"),
                                    PayloadDocumentation.fieldWithPath("error_code").type(JsonFieldType.STRING).description("에러 코드")
                            )
                    ));

            then(postService).should().postDetail(any(Long.class));
        }

    }

}
