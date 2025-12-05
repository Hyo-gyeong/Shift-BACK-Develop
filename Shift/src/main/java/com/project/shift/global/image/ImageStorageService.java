package com.project.shift.global.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    /**
     * 이미지를 저장하고, DB에 넣을 "저장 경로(키)"를 반환
     * 예) products/abc.png
     */
    String saveImage(MultipartFile file, String directory);

    /**
     * 저장 경로를 실제 접근 가능한 URL로 변환
     * 예) /images/** 매핑 → http://localhost:8080/images/products/uuid.png
     */
    String getImageUrl(String storedPath);

    /**
     * 필요 시 삭제용
     */
    default void deleteImage(String storedPath) {
    }
}
