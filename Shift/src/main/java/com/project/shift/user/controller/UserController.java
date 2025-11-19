package com.project.shift.user.controller;

import com.project.shift.user.dto.UserDTO;
import com.project.shift.user.dto.LoginIdRequestDTO;
import com.project.shift.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            //서버 회원가입 요청
            Long userId = userService.join(userDTO);

            //성공 응답(201 Created)
            return new ResponseEntity<>("회원가입 성공. 할당된 사용자 ID:" + userId, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            //클라이언트 오류 응답(409 Conflict)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            //서버 오류 응답(500 Internal Server Error)
            return new ResponseEntity<>("회원가입 중 서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 본인 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyInfo() {
        UserDTO user = userService.getUserInfo();
        return ResponseEntity.ok(user);
    }

    // 본인 정보 수정
    @PutMapping("/info")
    public ResponseEntity<UserDTO> updateMyInfo(
            @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserInfo(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // 아이디 찾기
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody LoginIdRequestDTO loginIdRequestDTO) {
        String loginId = userService.findId(loginIdRequestDTO);
        return ResponseEntity.ok(Map.of("loginId", loginId));
    }
}