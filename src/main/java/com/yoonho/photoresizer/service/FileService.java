package com.yoonho.photoresizer.service;

import com.yoonho.photoresizer.dto.FileDto;
import com.yoonho.photoresizer.exception.CustomIOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    public List<FileDto> convertMultipartToFile(List<MultipartFile> multipartFiles) {
        List<FileDto> result = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = multipartFile.getOriginalFilename();
            String filePath = uploadPath + "\\" + fileName;
            File file = new File(filePath);

            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                throw new CustomIOException("파일 업로드 중 오류가 발생하였습니다.", e);
            }

            FileDto fileDto = new FileDto(
                    fileName,
                    filePath);

            result.add(fileDto);
        }

        return result;
    }
}
