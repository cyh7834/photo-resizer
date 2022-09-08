package com.yoonho.photoresizer.photo.repository;

import com.yoonho.photoresizer.photo.dto.OldPhoto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public interface PhotoRepositoryCustom {
    String findResizePathByUUID(String uuid);
    List<OldPhoto> findOldPhoto(LocalDateTime localDateTime);
    void deletePhotoByIds(HashSet<Long> ids);
}
