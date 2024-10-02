package com.example.restdocs.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

@TestConfiguration
public class RestDocsConfiguration {

    @Bean
    public RestDocumentationResultHandler restDocumentationResultHandler() {
        return MockMvcRestDocumentation.document("{method-name}", // 문서 이름 설정
                // 요청 관련 헤더 공통 설정
                Preprocessors.preprocessRequest(
                        Preprocessors.modifyHeaders()
                                .remove("Content-Length")
                                .remove("Host"),
                        Preprocessors.prettyPrint()
                ),
                // 응답 관련 헤더 공통 설정
                Preprocessors.preprocessResponse(
                        Preprocessors.modifyHeaders()
                                .remove("Content-Length"),
                        Preprocessors.prettyPrint()
                )
        );
    }

}
