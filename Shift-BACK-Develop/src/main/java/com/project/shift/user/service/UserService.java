package com.project.shift.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.user.UserConstants;
import com.project.shift.user.dto.LoginIdRequestDTO;
import com.project.shift.user.dto.UserRegisterRequestDTO;
import com.project.shift.user.dto.UserResponseDTO;
import com.project.shift.user.dto.UserUpdateRequestDTO;
import com.project.shift.user.entity.UserEntity;
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
        validateName(userDTO);
        validateTermsAgreement(userDTO);
        validatePasswordRule(userDTO.getPassword());

        if (isLoginIdAvailable(userDTO.getLoginId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        if (isPhoneAvailable(userDTO.getPhone())) {
            throw new IllegalArgumentException("이미 사용중인 연락처입니다.");
        }

        UserEntity userEntity = convertToEntity(userDTO);
        return userRepository.save(userEntity).getUserId();
    }

    public boolean isLoginIdAvailable(String loginId) {
        if (loginId == null || loginId.trim().isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }
        if (loginId.length() < 4 || loginId.length() > 20) {
            throw new IllegalArgumentException("아이디는 4자 이상 20자 이하로 설정해야 합니다.");
        }
        if (!loginId.matches("^[A-Za-z0-9]+$")) {
            throw new IllegalArgumentException("아이디는 영문과 숫자만 사용할 수 있습니다.");
        }
        if (loginId.toLowerCase().startsWith(UserConstants.DELETED_USER_PREFIX)) {
            throw new IllegalArgumentException(
                "'" + UserConstants.DELETED_USER_PREFIX + "'로 시작하는 ID는 사용할 수 없습니다.");
        }
        return userRepository.existsByLoginId(loginId);
    }

    public boolean isPhoneAvailable(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("연락처를 입력해주세요.");
        }
        if (!phone.matches("^[0-9]{11}$")) {
            throw new IllegalArgumentException("연락처는 11자리 숫자만 입력 가능합니다.");
        }
        return userRepository.existsByPhone(phone);
    }

    public void validatePasswordRule(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해야 합니다.");
        }
        if (password.length() < 8 || password.length() > 24) {
            throw new IllegalArgumentException("비밀번호는 8자 이상 24자 이하로 설정해야 합니다.");
        }
        if (!password.matches("^[A-Za-z0-9!@#$%^&*()]+$")) {
            throw new IllegalArgumentException("비밀번호는 영문, 숫자, 특수문자만 사용할 수 있습니다.");
        }
        boolean hasUpperCase  = password.matches(".*[A-Z].*");
        boolean hasLowerCase  = password.matches(".*[a-z].*");
        boolean hasDigit      = password.matches(".*[0-9].*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()].*");
        if (!hasUpperCase || !hasLowerCase || !hasDigit || !hasSpecialChar) {
            throw new IllegalArgumentException(
                "비밀번호는 대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다.");
        }
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserInfo(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return toResponseDTO(userEntity);
    }

    @Transactional
    public UserResponseDTO updateUserInfo(Long userId, UserUpdateRequestDTO userDTO) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (!userEntity.getPhone().equals(userDTO.getPhone())
                && userRepository.existsByPhone(userDTO.getPhone())) {
            throw new IllegalArgumentException("이미 사용중인 연락처 입니다.");
        }

        userEntity.updateInfo(userDTO.getName(), userDTO.getPhone(), userDTO.getAddress());
        return toResponseDTO(userEntity);
    }

    @Transactional(readOnly = true)
    public String findId(LoginIdRequestDTO loginIdRequestDTO) {
        validateFindIdDTO(loginIdRequestDTO);
        UserEntity userEntity = userRepository
                .findByNameAndPhone(loginIdRequestDTO.name(), loginIdRequestDTO.phone())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 사용자가 없습니다."));
        return maskLoginId(userEntity.getLoginId());
    }

    @Transactional(readOnly = true)
    public boolean verifyPassword(Long userId, String password) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return passwordEncoder.matches(password, user.getPassword());
    }

    // 탈퇴 시 shop 도메인 처리는 WithdrawFacade로 위임
    // WithdrawFacade에서만 호출 — 유저 논리 삭제만 담당
    // 탈퇴 전 주문/배송/장바구니 처리는 WithdrawFacade 책임
    @Transactional
    public void markAsWithdrawn(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.withdraw();
    }

    // ── private 헬퍼 ──────────────────────────────────────────

    private void validateName(UserRegisterRequestDTO userDTO) {
        if (userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("이름을 입력해야 합니다.");
        }
        if (userDTO.getName().length() < 2 || userDTO.getName().length() > 6) {
            throw new IllegalArgumentException("이름은 2자 이상 6자 이하로 입력해야 합니다.");
        }
        if (!userDTO.getName().matches("^[가-힣\\s]+$")) {
            throw new IllegalArgumentException("이름은 한글만 사용할 수 있습니다.");
        }
    }

    private void validateTermsAgreement(UserRegisterRequestDTO userDTO) {
        if (userDTO.getTermsAgreed() == null || !userDTO.getTermsAgreed()) {
            throw new IllegalArgumentException("이용약관에 동의해야 합니다.");
        }
    }

    private void validateFindIdDTO(LoginIdRequestDTO dto) {
        if (dto.name() == null || dto.name().isBlank()) {
            throw new IllegalArgumentException("[SYSTEM] 이름은 필수 입력 항목입니다.");
        }
        if (dto.phone() == null || dto.phone().isBlank()) {
            throw new IllegalArgumentException("[SYSTEM] 연락처는 필수 입력 항목입니다.");
        }
    }

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