package com.sonfe.AppChat.exception;

public enum ErrorCode {
    UN_KNOW_ERROR(9999, "Lỗi không xác định"),
    INVALID_KEY_ERROR((9998), "Invalid message key"),

    USER_EXISTED(1001, "User existed"),
    USER_INVALID_USERNAME(1002, "Username min 3 charactors"),
    USER_INVALID_PASSWORD(1003, "Password min 8 charactors"),
    USER_INVALID_USERNAME_EMPTY(1004, "Username not empty"),
    USER_INVALID_PASSWORD_EMPTY(1005, "Password not empty"),
    USER_NOT_FOUND(1006, "User not found"),
    USER_NOT_EXISTED(1007, "User not existed"),

    UNAUTHENTICATED(2000, "Un authenticated"),

    INIT_STORAGE_ERROR(3000, "Could not initialize storage")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
