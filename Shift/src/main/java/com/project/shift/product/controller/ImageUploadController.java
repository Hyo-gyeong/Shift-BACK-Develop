package com.project.shift.product.controller;

import com.project.shift.global.image.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageStorageService imageStorageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "directory", defaultValue = "products") String directory
    ) {
        // 1) 로컬 저장 + 경로 반환
        String storedPath = imageStorageService.saveImage(file, directory);
        // 2) 접근 URL 생성
        String url = imageStorageService.getImageUrl(storedPath);

        ImageUploadResponse response = new ImageUploadResponse(storedPath, url);
        return ResponseEntity.ok(response);
    }

    public static record ImageUploadResponse(
            String storedPath, // DB images.image_url 에 넣을 값
            String url         // 화면에서 바로 쓸 수 있는 전체 URL
    ) {}
}
