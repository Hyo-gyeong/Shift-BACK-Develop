package com.project.shift.global.validation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.shift.global.validation.dto.InputValidationResponse;
import com.project.shift.global.validation.util.InputSecurityValidator;

@Service
public class SecurityValidationService {

    public InputValidationResponse validateInput(String value) {
        List<String> issues = InputSecurityValidator.validate(value);
        boolean safe = issues.isEmpty();
        return new InputValidationResponse(safe, issues);
    }
}
