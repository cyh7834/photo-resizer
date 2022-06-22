package com.yoonho.photoresizer.validator;

import com.yoonho.photoresizer.dto.DownloadDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Slf4j
@Component
public class DownloadValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(DownloadDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DownloadDto downloadDto = (DownloadDto) target;
        String uuid = downloadDto.getUuid();
        String pattern = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";
        String fileName = downloadDto.getFileName().toLowerCase();

        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
            log.error("jpg 외의 확장자 다운로드 요청");
            errors.reject("올바른 요청이 아닙니다.");
        }
        else if (!Pattern.matches(pattern, uuid)) {
            log.error("올바르지 않은 UUID 요청");
            errors.reject("올바른 요청이 아닙니다.");
        }
    }
}
