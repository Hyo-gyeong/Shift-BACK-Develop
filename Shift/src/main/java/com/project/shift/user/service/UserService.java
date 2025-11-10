package com.project.shift.user.service;

import com.project.shift.user.dao.IUserDAO;
import com.project.shift.user.dto.UserDTO;
import com.project.shift.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserDAO userDAO;

    @Transactional
    public Long join(UserDTO userDTO) {

        validateDuplicateUser(userDTO);
        validatePassword(userDTO);

        UserEntity userEntity = convertToEntity(userDTO);
        UserEntity savedEntity = userDAO.save(userEntity);

        return savedEntity.getUserId();
    }

        private void validateDuplicateUser(UserDTO userDTO){
            //아이디 중복 검증
            if (userDAO.existsByLoginId(userDTO.getLoginId())) {
                throw new IllegalArgumentException("이미 사용중인 아이디 입니다.");
            }
            //연락처 중복 검증
            if (userDAO.existsByPhone(userDTO.getPhone())) {
                throw new IllegalArgumentException("이미 사용중인 연락처 입니다.");
            }
        }

        private void validatePassword(UserDTO userDTO) {
            //비밀번호 보안 규칙 검증
            String password = userDTO.getPassword();
            boolean isValid = password.length() >= 8 &&
                    password.matches(".*[A-Z].*") &&     // 대문자 포함
                    password.matches(".*[a-z].*") &&     // 소문자 포함
                    password.matches(".*\\d.*") &&       // 숫자 포함
                    password.matches(".*[!@#$%^&*()].*"); // 특수문자 포함

            if (!isValid) {
                throw new IllegalArgumentException(
                        "비밀번호는 최소 8자 이상이어야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.");
            }
        }

        //DTO를 Entitiy로 변환 및 암호화된 비밀번호 설정
        private UserEntity convertToEntity(UserDTO userDTO) {
            return UserEntity.builder()
                    .loginId(userDTO.getLoginId())
                    .password(userDTO.getPassword())
                    .name(userDTO.getName())
                    .phone(userDTO.getPhone())
                    .address(userDTO.getAddress())
                    .adminFlag("N")
                    .build();
    }
}
