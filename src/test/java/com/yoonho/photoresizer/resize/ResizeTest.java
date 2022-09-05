package com.yoonho.photoresizer.resize;

import com.google.gson.Gson;
import com.yoonho.photoresizer.dto.FileDto;
import com.yoonho.photoresizer.dto.ResponseDto;
import com.yoonho.photoresizer.exception.CustomNotJpgException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(value = PER_CLASS)
@AutoConfigureMockMvc
public class ResizeTest {
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    MockMvc mockMvc;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Value("${resize.file.path}")
    private String resizeFilePath;

    @Test
    @DisplayName("정상적인 변환 테스트")
    void resizeTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(getRequestBuilder("normal.jpg", "image/jpg"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Gson gson = new Gson();

        ResponseDto response = gson.fromJson(content, ResponseDto.class);
        FileDto fileDto = gson.fromJson(gson.toJson(response.getData()), FileDto.class);
        String uuid = fileDto.getUuid();
        String originFileName = fileDto.getFileName();
        String fileName = uuid + "_" + originFileName;

        Assertions.assertTrue(Files.exists(Paths.get(uploadPath + fileName)));
        Assertions.assertTrue(Files.exists(Paths.get(resizeFilePath + fileName)));
    }

    @Test
    @DisplayName("지원하지 않는 포맷 변환 요청 예외처리 테스트")
    void resizeNotSupportedFormatTest() throws Exception {
        mockMvc.perform(getRequestBuilder("png.png", "image/png"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("20MB 초과 파일 변환 요청 예외처리 테스트")
    void resizeLargeFileTest() throws Exception {
        mockMvc.perform(getRequestBuilder("large.jpg", "image/jpg"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("가짜 확장자 변환 요청 예외처리 테스트")
    void resizeFakeJpgFileTest() throws Exception {
        mockMvc.perform(getRequestBuilder("fake-jpg.jpg", "image/jpg"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomNotJpgException))
                .andDo(print());
    }

    private RequestBuilder getRequestBuilder(String testFileName, String contentType) throws IOException {
        File imageFile = new File("src/test/resources/static/img/" + testFileName);

        MockMultipartFile file =
                new MockMultipartFile("file", testFileName, contentType, new FileInputStream(imageFile));

        return MockMvcRequestBuilders.multipart("/resize").file(file);
    }
}
