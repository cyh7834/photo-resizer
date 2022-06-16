package com.yoonho.photoresizer.service;

import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.yoonho.photoresizer.dto.FileDto;
import com.yoonho.photoresizer.exception.CustomErrorPageException;
import com.yoonho.photoresizer.exception.CustomIOException;
import com.yoonho.photoresizer.exception.CustomNotJpgException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FileService {
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Value("${resize.file.path}")
    private String resizeFilePath;

    private static final int DELETE_SEC = 3600;

    public FileDto convertMultipartToFile(MultipartFile multipartFile) {
        String uuid = UUID.randomUUID().toString();
        String fileName =  multipartFile.getOriginalFilename();
        String filePath = uploadPath + "\\" + uuid + "_" + fileName;
        File file = new File(filePath);

        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new CustomIOException("파일 업로드 중 오류가 발생하였습니다.", e);
        }

        return new FileDto(uuid, fileName, null);
    }

    public void checkJpgFileType(FileDto fileDto) {
        String uuid = fileDto.getUuid();
        String fileName = fileDto.getFileName();
        String savedName = uuid + "_" + fileName;
        String filePath = uploadPath + "\\" + savedName;
        File file = new File(filePath);

        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
            FileType fileType = FileTypeDetector.detectFileType(stream);

            if (fileType != FileType.Jpeg) {
                throw new CustomNotJpgException("올바른 형식의 JPG 파일이 아닙니다.");
            }
        } catch (FileNotFoundException e) {
            throw new CustomIOException("파일 로드 중 오류가 발생하였습니다.", e);
        } catch (IOException e) {
            throw new CustomIOException("파일 타입 추출 중 오류가 발생하였습니다.", e);
        }
    }

    public String getContentType(Path path) {
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            throw new CustomErrorPageException("500", e);
        }
    }

    public void deleteOldFile() {
        File resizeDir = new File(resizeFilePath);

        File[] resizeFiles = resizeDir.listFiles();
        if (resizeFiles != null) {
            for (File resizeFile : resizeFiles) {
                try {
                    long secondsFromModification = getSecondsFromModification(resizeFile);

                    if (secondsFromModification > DELETE_SEC) {
                        String fileName = resizeFile.getName();
                        log.info("리소스 파일 삭제 : " + fileName);
                        resizeFile.delete();
                        new File(uploadPath + "\\" + fileName).delete();
                    }
                } catch (IOException e) {
                    log.error("업로드 파일 삭제 여부 확인 중 오류가 발생하였습니다.");
                    e.printStackTrace();
                }
            }
        }

    }

    private long getSecondsFromModification(File file) throws IOException {
        Path path = file.toPath();
        BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);

        return (System.currentTimeMillis() - basicFileAttributes.lastModifiedTime().to(TimeUnit.MILLISECONDS)) / 1000;
    }
}
