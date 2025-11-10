//package com.project.shift.product.controller;
//
//import com.project.shift.product.repository.ImageRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/images")
//public class ImageController {
//
//    @Autowired
//    private ImageRepository imageRepository;
//
//    // 대표 이미지 설정
//    @PostMapping("/set-representative")
//    public void setRepresentativeImage(@RequestParam Long productId, @RequestParam Long imageId) {
//        imageRepository.updateIsRepresentativeByProductId(productId, "N");  // 다른 이미지들을 'N'으로 설정
//        imageRepository.updateIsRepresentativeByImageId(imageId, "Y");  // 새로운 대표 이미지를 'Y'로 설정
//    }
//} develop에는 ImageController가 없어서 우선 주석 처리함 - 신효경