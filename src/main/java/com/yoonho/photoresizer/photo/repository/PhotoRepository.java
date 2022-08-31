package com.yoonho.photoresizer.photo.repository;

import com.yoonho.photoresizer.photo.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long>, PhotoRepositoryCustom {
    Photo findByUuid(String uuid);
}
