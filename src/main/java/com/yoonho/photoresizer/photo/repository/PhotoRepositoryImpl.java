package com.yoonho.photoresizer.photo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yoonho.photoresizer.photo.dto.OldPhoto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static com.yoonho.photoresizer.photo.domain.QPhoto.*;

@RequiredArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<OldPhoto> findOldPhoto(LocalDateTime localDateTime) {
        LocalDateTime notResizedTime = localDateTime.minusMinutes(15);
        LocalDateTime resizedTime = localDateTime.minusHours(1);

        return queryFactory
                .select(Projections.constructor(OldPhoto.class,
                        photo.id,
                        photo.uploadPath,
                        photo.resizePath))
                .from(photo)
                .where(photo.uploadedAt.lt(notResizedTime)
                        .and(photo.resizePath.isNull())
                        .or(photo.uploadedAt.lt(resizedTime)
                        .and(photo.resizePath.isNotNull())))
                .fetch();
    }

    @Override
    public void deletePhotoByIds(HashSet<Long> ids) {
        queryFactory
                .delete(photo)
                .where(photo.id.in(ids))
                .execute();
    }
}
