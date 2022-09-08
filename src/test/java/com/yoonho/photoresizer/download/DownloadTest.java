package com.yoonho.photoresizer.download;

import com.yoonho.photoresizer.exception.CustomErrorPageException;
import com.yoonho.photoresizer.photo.domain.Photo;
import com.yoonho.photoresizer.photo.service.PhotoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DownloadTest {
    @Autowired
    MockMvc mockMvc;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Value("${resize.file.path}")
    private String resizeFilePath;

    @Autowired
    PhotoService photoService;

    @Test
    @DisplayName("정상적인 다운로드 테스트")
    public void downloadTest() throws Exception {
        String uuid = UUID.randomUUID().toString();
        String fileName = "normal.jpg";
        String testFilePath = "src/test/resources/static/img/" + fileName;
        String uploadFilePath = uploadPath + uuid + "_" + fileName;
        String resourceFilePath = resizeFilePath + uuid + "_" + fileName;

        Assertions.assertTrue(copyTestFile(testFilePath, uploadFilePath, resourceFilePath));

        Photo photo = new Photo();
        photo.setUuid(uuid);
        photo.setUploadPath(uploadFilePath);
        photo.setResizePath(resourceFilePath);
        photo.setResizedAt(LocalDateTime.now());

        photoService.insertPhoto(photo);

        mockMvc.perform(
                get("/download")
                        .param("uuid", uuid)
                        .param("fileName", fileName))
                .andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Disposition", "attachment; filename*=UTF-8''resized_" + fileName))
                .andExpect(header().stringValues("Content-Type", "image/jpeg"));
    }

    @Test
    @DisplayName("다른 확장자의 파일 다운로드 테스트")
    public void downloadNotJpgTest() throws Exception {
        mockMvc.perform(
                get("/download")
                        .param("uuid", UUID.randomUUID().toString())
                        .param("fileName", "testFileName.js"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomErrorPageException))
                .andExpect(view().name("error/error"))
                .andDo(print());
    }

    @Test
    @DisplayName("올바르지 않은 UUID의 파일 다운로드 테스트")
    public void downloadNotValidUuidTest() throws Exception {
        mockMvc.perform(
                get("/download")
                        .param("uuid", "not valid uuid")
                        .param("fileName", "testFileName.jpg"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomErrorPageException))
                .andExpect(view().name("error/error"))
                .andDo(print());
    }

    private boolean copyTestFile(String testFilePath, String uploadFilePath, String resourceFilePath) {
        File testFile = new File(testFilePath);
        File uploadFile = new File(uploadFilePath);
        File resourceFile = new File(resourceFilePath);

        try {
            Files.copy(testFile.toPath(), uploadFile.toPath(), REPLACE_EXISTING);
            Files.copy(testFile.toPath(), resourceFile.toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }
}
