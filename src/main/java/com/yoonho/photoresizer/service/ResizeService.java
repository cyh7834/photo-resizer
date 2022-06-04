package com.yoonho.photoresizer.service;

import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.yoonho.photoresizer.dto.FileDto;
import com.yoonho.photoresizer.exception.CustomIOException;
import com.yoonho.photoresizer.exception.CustomImageProcessingException;
import com.yoonho.photoresizer.exception.CustomMetadataException;
import com.yoonho.photoresizer.exception.CustomNotJpgException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class ResizeService {
    @Value("${resize.file.path}")
    private String resizeFilePath;

    public String resizeJpg(FileDto fileDto) {
        String filePath = fileDto.getFilePath();
        File file = new File(filePath);

        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
            FileType fileType = FileTypeDetector.detectFileType(stream);

            if (fileType != FileType.Jpeg) {
                throw new CustomNotJpgException("올바른 형식의 JPG 파일이 아닙니다.");
            }
        } catch (FileNotFoundException e) {
            throw new CustomIOException("파일 로드 중 오류가 발생하였습니다.", e);
        } catch (IOException e) {
            throw new CustomIOException("파일 타입 추출 중 오류가 발생하였습니다.", e);
        }

        int orientation = getOrientation(file);
        BufferedImage bufferedImage = rotateImage(file, orientation);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight(null);
        int whitePixel, squareSize;

        if (width > height) {
            whitePixel = (int) (width - height) / 2;
            squareSize = width;
        }
        else {
            whitePixel = (int) (height - width) / 2;
            squareSize = height;
        }

        BufferedImage newImage = new BufferedImage(squareSize, squareSize, bufferedImage.getType());

        for (int x = 0; x < squareSize; x++) {
            for (int y = 0; y < squareSize; y++) {
                if (squareSize == width) {
                    if (y < whitePixel || y >= height + whitePixel) { //white pixel zone
                        newImage.setRGB(x, y, 0xFFFFFF);
                    }
                    else {
                        newImage.setRGB(x, y, bufferedImage.getRGB(x, y - whitePixel));
                    }
                }
                else {
                    if (x < whitePixel || x >= width + whitePixel) { //white pixel zone
                        newImage.setRGB(x, y, 0xFFFFFF);
                    }
                    else {
                        newImage.setRGB(x, y, bufferedImage.getRGB(x - whitePixel, y));
                    }
                }
            }
        }

        try {
            String fileName = fileDto.getFileName();

            ImageIO.write(newImage, "JPG", new File(resizeFilePath, fileName));
            return fileName;
        } catch (IOException e) {
            throw new CustomNotJpgException("올바른 형식의 JPG 파일이 아닙니다.", e);
        }
    }

    private int getOrientation(File file) {
        int orientation = 1;

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }

            return orientation;
        } catch (ImageProcessingException e) {
            throw new CustomImageProcessingException("파일 메타 데이터 확인 중 오류가 발생하였습니다.", e);
        } catch (IOException e) {
            throw new CustomIOException("파일 로드 중 오류가 발생하였습니다.", e);
        } catch (MetadataException e) {
            throw new CustomMetadataException("파일 메타 데이터 추출 중 오류가 발생하였습니다.", e);
        }
    }

    private BufferedImage rotateImage(File file, int orientation) {
        BufferedImage bufferedImage = null;

        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            throw new CustomIOException("파일 변환 중 오류가 발생하였습니다.", e);
        }

        if (orientation == 1) { // 정위치
            return bufferedImage;
        }
        else if (orientation == 6) {
            return rotateImage(bufferedImage, 90);
        }
        else if (orientation == 3) {
            return rotateImage(bufferedImage, 180);
        }
        else if (orientation == 8) {
            return rotateImage(bufferedImage, 270);
        }
        else {
            return bufferedImage;
        }
    }

    private BufferedImage rotateImage(BufferedImage bufferedImage, int radians) {
        BufferedImage newImage;

        int bufferedImageHeight = bufferedImage.getHeight();
        int bufferedImageWidth = bufferedImage.getWidth();

        if (radians == 90 || radians == 270) {
            newImage = new BufferedImage(bufferedImageHeight, bufferedImageWidth, bufferedImage.getType());
        }
        else if (radians == 180) {
            newImage = new BufferedImage(bufferedImageWidth, bufferedImageHeight, bufferedImage.getType());
        }
        else {
            return bufferedImage;
        }

        Graphics2D graphics = (Graphics2D) newImage.getGraphics();
        int newImageWidth = newImage.getWidth();
        int newImageHeight = newImage.getHeight();

        graphics.rotate(Math.toRadians(radians), newImageWidth / 2, newImageHeight / 2);
        graphics.translate((newImageWidth - bufferedImageWidth) / 2, (newImageHeight - bufferedImageHeight) / 2);
        graphics.drawImage(bufferedImage, 0, 0, bufferedImageWidth, bufferedImageHeight, null);

        return newImage;
    }
}
