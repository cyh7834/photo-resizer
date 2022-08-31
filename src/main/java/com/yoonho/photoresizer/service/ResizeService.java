package com.yoonho.photoresizer.service;

import com.yoonho.photoresizer.dto.FileDto;
import com.yoonho.photoresizer.exception.CustomNotJpgException;
import com.yoonho.photoresizer.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class ResizeService {
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Value("${resize.file.path}")
    private String resizeFilePath;

    private final PhotoService photoService;

    public void resizeJpg(FileDto fileDto) {
        String uuid = fileDto.getUuid();
        String fileName = fileDto.getFileName();
        String savedName = uuid + "_" + fileName;
        String filePath = uploadPath + savedName;
        String resultPath = resizeFilePath + savedName;
        File file = new File(filePath);

        try {
            BufferedImage squareImage = createSquareBufferedImage(ImageIO.read(file));
            JPEGImageWriteParam jpegParams = getJpegImageWriteParam();
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            File savedFile = new File(resultPath);
            IIOMetadata metadata = getMetadata(file);
            BufferedImage resizedImage = Scalr.resize(squareImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, 1080);
            IIOImage resultImage = new IIOImage(resizedImage, null, metadata);

            writer.setOutput(new FileImageOutputStream(savedFile));
            writer.write(null, resultImage, jpegParams);

            fileDto.setFileSize(getFileSizeAsString(savedFile));

            photoService.updateResizePhoto(uuid, resultPath);
        } catch (IOException e) {
            throw new CustomNotJpgException("Not a valid jpg file.", e);
        }
    }

    private BufferedImage createSquareBufferedImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight(null);
        int whitePixel = Math.abs(width - height) / 2;
        int squareSize = Math.max(width, height);

        BufferedImage newImage = new BufferedImage(squareSize, squareSize, bufferedImage.getType());
        Graphics2D graphics = newImage.createGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, squareSize, squareSize);

        if (squareSize == width) {
            graphics.drawImage(bufferedImage, 0, whitePixel, null);
        }
        else {
            graphics.drawImage(bufferedImage, whitePixel, 0, null);
        }

        graphics.dispose();

        return newImage;
    }

    private JPEGImageWriteParam getJpegImageWriteParam() {
        JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
        jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(1f);
        jpegParams.setCompressionType("JPEG");
        jpegParams.setOptimizeHuffmanTables(true);

        return jpegParams;
    }

    private IIOMetadata getMetadata(File file) throws IOException {
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(file);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);
        ImageReader reader = readers.next();

        reader.setInput(imageInputStream, true);

        return reader.getImageMetadata(0);
    }

    private String getFileSizeAsString(File savedFile) {
        long bytes = savedFile.length();
        long kilobyte = bytes / 1024;
        long megabyte = kilobyte / 1024;

        return megabyte + "." + (kilobyte - (megabyte * 1024)) / 100 + " MB";
    }
}
