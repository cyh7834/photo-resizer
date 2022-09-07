package com.yoonho.photoresizer.file.service;

import com.yoonho.photoresizer.file.dto.FileDto;
import com.yoonho.photoresizer.exception.CustomErrorPageException;
import com.yoonho.photoresizer.exception.CustomIOException;
import com.yoonho.photoresizer.photo.domain.Photo;
import com.yoonho.photoresizer.photo.dto.OldPhoto;
import com.yoonho.photoresizer.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final PhotoService photoService;

    public FileDto convertMultipartToFile(MultipartFile multipartFile) {
        String uuid = UUID.randomUUID().toString();
        String fileName =  multipartFile.getOriginalFilename();
        String filePath = uploadPath + uuid + "_" + fileName;
        File file = new File(filePath);

        try {
            multipartFile.transferTo(file);

            Photo photo = new Photo();
            photo.setUuid(uuid);
            photo.setUploadPath(filePath);

            photoService.insertPhoto(photo);
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
        List<OldPhoto> oldPhotos = photoService.selectOldPhoto();
        HashSet<Long> deletedPhotoId = new HashSet<>();

        for (OldPhoto oldPhoto : oldPhotos) {
            String uploadPath = oldPhoto.getUploadPath();
            String resizePath = oldPhoto.getResizePath();
            Long id = oldPhoto.getId();

            try {
                Files.deleteIfExists(Paths.get(uploadPath));
                log.info("업로드 리소스 파일 삭제 : " + uploadPath);

                if (resizePath == null) {
                    deletedPhotoId.add(id);
                    continue;
                }

                Files.deleteIfExists(Paths.get(resizePath));
                log.info("변환 리소스 파일 삭제 : " + resizePath);
                deletedPhotoId.add(id);
            } catch (IOException e) {
                log.error("업로드 파일 삭제 여부 확인 중 오류가 발생하였습니다.");
                deletedPhotoId.remove(id);
                e.printStackTrace();
            }
        }

        photoService.deletePhotoByIds(deletedPhotoId);
    }
}
