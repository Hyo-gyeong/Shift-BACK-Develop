package com.project.shift.global.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class LocalImageStorageService implements ImageStorageService {

    private final Path uploadRoot;
    private final String baseUrl;

    public LocalImageStorageService(
            @Value("${app.image.upload-dir}") String uploadDir,
            @Value("${app.image.base-url}") String baseUrl
    ) {
        // uploadDir = "uploads" 같이 상대경로면, 실행 디렉토리 기준으로 절대경로로 변환
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.baseUrl = baseUrl;

        try {
            Files.createDirectories(this.uploadRoot);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 폴더를 생성할 수 없습니다: " + uploadDir, e);
        }
    }

    @Override
    public String saveImage(MultipartFile file, String directory) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 업로드할 수 없습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String filename = UUID.randomUUID().toString() + ext; // uuid.png
        String relativePath = directory + "/" + filename;     // products/uuid.png

        try {
            Path targetLocation = this.uploadRoot.resolve(relativePath).normalize();
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 중 오류가 발생했습니다.", e);
        }

        // DB에는 이 값(relativePath)을 그대로 넣는다
        return relativePath;
    }

    @Override
    public String getImageUrl(String storedPath) {
        if (storedPath == null || storedPath.isBlank()) return null;
        // baseUrl = http://localhost:8080/images
        return baseUrl + "/" + storedPath.replace("\\", "/");
    }
}
