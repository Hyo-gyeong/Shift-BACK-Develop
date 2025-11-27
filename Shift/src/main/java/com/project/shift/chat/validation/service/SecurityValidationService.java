package com.project.shift.chat.validation.service;

import com.project.shift.chat.validation.dto.InputValidationResponse;
import com.project.shift.chat.validation.util.InputSecurityValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityValidationService {

    public InputValidationResponse validateInput(String value) {
        List<String> issues = InputSecurityValidator.validate(value);
        boolean safe = issues.isEmpty();
        return new InputValidationResponse(safe, issues);
    }
}
