package com.yoonho.photoresizer.photo.service;

import com.yoonho.photoresizer.photo.dto.OldPhoto;
import com.yoonho.photoresizer.photo.domain.Photo;
import com.yoonho.photoresizer.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    @Transactional
    public void insertPhoto(Photo photo) {
        photoRepository.save(photo);
    }

    @Transactional
    public void updateResizePhoto(String uuid, String resizePath) {
        Photo photo = photoRepository.findByUuid(uuid);
        photo.setResizePath(resizePath);
        photo.setResizedAt(LocalDateTime.now());
    }

    @Transactional
    public String findResizePathByUUID(String uuid) {
        return photoRepository.findResizePathByUUID(uuid);
    }

    @Transactional(readOnly = true)
    public List<OldPhoto> selectOldPhoto() {
        return photoRepository.findOldPhoto(LocalDateTime.now());
    }

    @Transactional
    public void deletePhotoByIds(HashSet<Long> ids) {
        photoRepository.deletePhotoByIds(ids);
    }
}
