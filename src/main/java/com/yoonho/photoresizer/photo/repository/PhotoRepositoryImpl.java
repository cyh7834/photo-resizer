package com.yoonho.photoresizer.photo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    public String findResizePathByUUID(String uuid) {
        return queryFactory
                .select(photo.resizePath)
                .from(photo)
                .where(photoUuidEq(uuid))
                .fetchOne();
    }

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

    private BooleanExpression photoUuidEq(String uuid) {
        return uuid != null ? photo.uuid.eq(uuid) : null;
    }
}
