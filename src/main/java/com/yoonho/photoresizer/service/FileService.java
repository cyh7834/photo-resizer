package com.yoonho.photoresizer.service;

import com.yoonho.photoresizer.dto.FileDto;
import com.yoonho.photoresizer.exception.CustomErrorPageException;
import com.yoonho.photoresizer.exception.CustomIOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class FileService {
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Value("${working.directory.path}")
    private String workingDirectoryPath;

    private static final int DELETE_SEC = 3600;

    public FileDto convertMultipartToFile(MultipartFile multipartFile) {
        String uuid = UUID.randomUUID().toString();
        String fileName =  multipartFile.getOriginalFilename();
        String filePath = uploadPath + uuid + "_" + fileName;
        File file = new File(filePath);

        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new CustomIOException("An error occurred while uploading the file.", e);
        }

        return new FileDto(uuid, fileName, null);
    }

    public String getContentType(Path path) {
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            throw new CustomErrorPageException("500", e);
        }
    }

    public void deleteOldFile() {
        Path dirPath = Paths.get(workingDirectoryPath);
        List<Path> result;

        try {
            Stream<Path> walk = Files.walk(dirPath);
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            for (Path path : result) {
                long secondsFromModification = getSecondsFromModification(path);

                if (secondsFromModification > DELETE_SEC) {
                    log.info("????????? ?????? ?????? : " + path.getFileName());
                    Files.deleteIfExists(path);
                }
            }
        } catch (IOException e) {
            log.error("????????? ?????? ?????? ?????? ?????? ??? ????????? ?????????????????????.");
            e.printStackTrace();
        }
    }

    private long getSecondsFromModification(Path path) throws IOException {
        BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);

        return (System.currentTimeMillis() - basicFileAttributes.lastModifiedTime().to(TimeUnit.MILLISECONDS)) / 1000;
    }
}
