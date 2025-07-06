package com.joon.sunguard_api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor // final 필드만 포함하는 생성자를 만듭니다.
public class ErrorResponse {
    private final int status;
    private final String message;
}
