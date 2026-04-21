package com.project.shift.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.user.UserConstants;
import com.project.shift.user.dto.request.LoginIdRequestDTO;
import com.project.shift.user.dto.request.UserRegisterRequestDTO;
import com.project.shift.user.dto.request.UserUpdateRequestDTO;
import com.project.shift.user.dto.response.UserResponseDTO;
import com.project.shift.user.entity.UserEntity;
import com.project.shift.user.exception.DuplicatePhoneException;
import com.project.shift.user.exception.UserNotFoundException;
import com.project.shift.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(UserRegisterRequestDTO userDTO) {
        UserEntity userEntity = convertToEntity(userDTO);
        return userRepository.save(userEntity).getUserId();
    }

    // DELETED_USER_PREFIX 체크는 형식이 아닌 비즈니스 규칙이므로 Service에 유지
    public boolean isLoginIdAvailable(String loginId) {
        if (loginId.toLowerCase().startsWith(UserConstants.DELETED_USER_PREFIX)) {
            throw new IllegalArgumentException(
                "'" + UserConstants.DELETED_USER_PREFIX + "'로 시작하는 ID는 사용할 수 없습니다.");
        }
        return userRepository.existsByLoginId(loginId);
    }

    // 중복 여부 확인은 비즈니스 로직이므로 유지
    public boolean isPhoneAvailable(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserInfo(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return toResponseDTO(userEntity);
    }

    @Transactional
    public UserResponseDTO updateUserInfo(Long userId, UserUpdateRequestDTO userDTO) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!userEntity.getPhone().equals(userDTO.getPhone())
                && userRepository.existsByPhone(userDTO.getPhone())) {
            throw new DuplicatePhoneException(userDTO.getPhone());
        }

        userEntity.updateInfo(userDTO.getName(), userDTO.getPhone(), userDTO.getAddress());
        return toResponseDTO(userEntity);
    }

    @Transactional(readOnly = true)
    public String findId(LoginIdRequestDTO loginIdRequestDTO) {
        UserEntity userEntity = userRepository
                .findByNameAndPhone(loginIdRequestDTO.name(), loginIdRequestDTO.phone())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 사용자가 없습니다."));
        return maskLoginId(userEntity.getLoginId());
    }

    @Transactional(readOnly = true)
    public boolean verifyPassword(Long userId, String password) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return passwordEncoder.matches(password, user.getPassword());
    }

    // WithdrawFacade에서만 호출 — 유저 논리 삭제만 담당
    @Transactional
    public void markAsWithdrawn(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.withdraw();
    }

    // ── private 헬퍼 ──────────────────────────────────────────

    private String maskLoginId(String loginId) {
        int length = loginId.length();
        int maskLength = length / 2;
        return loginId.substring(0, length - maskLength) + "*".repeat(maskLength);
    }

    private UserEntity convertToEntity(UserRegisterRequestDTO userDTO) {
        return UserEntity.builder()
                .loginId(userDTO.getLoginId())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .name(userDTO.getName())
                .phone(userDTO.getPhone())
                .address(userDTO.getAddress())
                .points(0)
                .adminFlag("N")
                .build();
    }

    private UserResponseDTO toResponseDTO(UserEntity entity) {
        return UserResponseDTO.builder()
                .userId(entity.getUserId())
                .loginId(entity.getLoginId())
                .name(entity.getName())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .points(entity.getPoints())
                .build();
    }
}