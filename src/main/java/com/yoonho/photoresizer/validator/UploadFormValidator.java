package com.yoonho.photoresizer.validator;

import com.yoonho.photoresizer.dto.UploadDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class UploadFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UploadDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UploadDto uploadDto = (UploadDto) target;
        MultipartFile multipartFile = uploadDto.getFile();
        String contentType = multipartFile.getContentType();
        long size = multipartFile.getSize();
        long sizeInMb = size / (1024 * 1024);

        if (contentType == null || !isSupportedContentType(contentType)) {
            log.error("지원하지 않는 확장자의 변환 요청");
            errors.reject("지원하지 않는 확장자 입니다.");
        }

        if (sizeInMb > 20) {
            log.error("20MB 이상의 파일 변환 요청");
            errors.reject("20MB 이하의 파일만 변환이 가능합니다.");
        }
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }
}
