package com.project.shift.chat.validation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.chat.validation.dto.InputValidationRequest;
import com.project.shift.chat.validation.service.SecurityValidationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security")
public class SecurityValidationController {

    private final SecurityValidationService validationService;

    @PostMapping("/validate-input")
    public ResponseEntity<?> validateInput(@RequestBody InputValidationRequest request) {
        return ResponseEntity.ok(
                validationService.validateInput(request.getInput())
        );
    }
}
