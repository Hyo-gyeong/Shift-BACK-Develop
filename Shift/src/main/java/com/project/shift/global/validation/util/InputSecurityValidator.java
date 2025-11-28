package com.project.shift.global.validation.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InputSecurityValidator {

	// 간단한 SQL 인젝션 패턴 (주석/따옴표/인코딩 포함)
    private static final Pattern SQL_INJECTION =
            Pattern.compile("('.+--)|(--)|(%7C)", Pattern.CASE_INSENSITIVE);

    // SQL 키워드(단순 탐지)
    private static final Pattern SQL_KEYWORDS =
            Pattern.compile("\\b(SELECT|INSERT|DELETE|UPDATE|DROP|ALTER|TRUNCATE)\\b",
                    Pattern.CASE_INSENSITIVE);

    // 단순 스크립트 태그 탐지 (XSS)
    private static final Pattern XSS_SCRIPT =
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);

    // HTML 태그 전반 탐지
    private static final Pattern HTML_TAG =
            Pattern.compile("<[^>]+>");

    // onclick, onerror 등 inline event handlers
    private static final Pattern JS_EVENT_HANDLER =
            Pattern.compile("on\\w+\\s*=", Pattern.CASE_INSENSITIVE);

    // 명령어 인젝션 관련 기호 (간단 탐지)
    private static final Pattern COMMAND_INJECTION =
            Pattern.compile("(&&|\\|\\||;)", Pattern.CASE_INSENSITIVE);
    // 경로 조작 패턴 (../)
    private static final Pattern PATH_TRAVERSAL =
            Pattern.compile("\\.\\./");

    public static List<String> validate(String input) {
        List<String> issues = new ArrayList<>();
        if (input == null) {
            issues.add("NULL 값은 허용되지 않습니다.");
            return issues;
        }
        if (input.length() > 30) {
            issues.add("입력 길이는 30자 이하입니다.");
        }
        if (SQL_INJECTION.matcher(input).find()) {
        	issues.add("SQL Injection 의심 패턴 감지");
        }
        if (SQL_KEYWORDS.matcher(input).find()) {
        	issues.add("SQL 키워드 패턴 감지");
        }
        if (XSS_SCRIPT.matcher(input).find()) {
        	issues.add("XSS 스크립트 패턴 감지");
        }
        if (HTML_TAG.matcher(input).find()) {
        	issues.add("HTML 태그 포함");
        }
        if (JS_EVENT_HANDLER.matcher(input).find()) {
        	issues.add("JavaScript 이벤트 핸들러 포함");
        }
        if (COMMAND_INJECTION.matcher(input).find()) {
        	issues.add("명령어 Injection 위험");
        }
        if (PATH_TRAVERSAL.matcher(input).find()) {
        	issues.add("경로 조작 위험 감지");
        }

        return issues;
    }
}