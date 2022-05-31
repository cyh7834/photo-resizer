package com.yoonho.photoresizer.validator;

import com.yoonho.photoresizer.dto.UploadDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class UploadFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UploadDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UploadDto uploadDto = (UploadDto) target;
        List<MultipartFile> multipartFiles = uploadDto.getFiles();

        for (MultipartFile multipartFile : multipartFiles) {
            String contentType = multipartFile.getContentType();

            if (contentType == null || !isSupportedContentType(contentType)) {
                errors.rejectValue("files", "invalid.contentType", "지원하지 않는 확장자 입니다.");
            }
        }
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }
}
