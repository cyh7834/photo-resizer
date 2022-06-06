package com.yoonho.photoresizer.validator;

import com.yoonho.photoresizer.dto.DownloadDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

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
            errors.rejectValue("fileName", "invalid.fileName", "올바른 확장자가 아닙니다.");
        }

        if (!Pattern.matches(pattern, uuid)) {
            errors.rejectValue("uuid", "invalid.uuid", "올바른 uuid가 아닙니다.");
        }
    }
}
