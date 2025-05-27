package com.ixfp.gitmon.controller.type

import org.springframework.http.HttpStatus

enum class ApiError(val httpStatus: HttpStatus, val message: String) {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "forbidden"),
    CONFLICT(HttpStatus.CONFLICT, "conflict"),
    REPOSITORY_NOT_CONFIGURED(HttpStatus.CONFLICT, "repository not configured"),
    INVALID_AUTH_CODE(HttpStatus.UNAUTHORIZED, "Invalid auth code"),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid access token"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid refresh token"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access token expired"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
}
