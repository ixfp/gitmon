package com.ixfp.gitmon.controller.util

import com.ixfp.gitmon.controller.type.ApiError
import com.ixfp.gitmon.controller.type.ApiResponseBody
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import java.time.Duration

object ApiResponseHelper {
    fun <T> success(data: T? = null): ResponseEntity<ApiResponseBody<T>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponseBody.success(HttpStatus.OK, data))
    }

    fun <T> created(data: T? = null): ResponseEntity<ApiResponseBody<T>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponseBody.success(HttpStatus.CREATED, data))
    }

    fun redirectPermanent(location: String): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.MOVED_PERMANENTLY)
            .header(HttpHeaders.LOCATION, location)
            .build()
    }

    fun <T> custom(
        status: HttpStatus,
        data: T? = null,
    ): ResponseEntity<ApiResponseBody<T>> {
        return ResponseEntity
            .status(status)
            .body(ApiResponseBody.success(status, data))
    }

    fun <T> error(
        apiError: ApiError,
        data: T? = null,
    ): ResponseEntity<ApiResponseBody<T>> {
        return ResponseEntity
            .status(apiError.httpStatus)
            .body(ApiResponseBody.error(apiError, data))
    }
}

fun <T> ResponseEntity<ApiResponseBody<T>>.withCookie(
    key: String,
    value: String,
    maxAge: Duration = Duration.ofDays(14),
    path: String = "/api/v1/refresh",
    httpOnly: Boolean = true,
    secure: Boolean = true,
    sameSite: String = "Lax",
): ResponseEntity<ApiResponseBody<T>> {
    val cookie =
        ResponseCookie.from(key, value)
            .path(path)
            .httpOnly(httpOnly)
            .secure(secure)
            .sameSite(sameSite)
            .apply { maxAge?.let { maxAge(it) } }
            .build()

    val headers =
        HttpHeaders().apply {
            putAll(this@withCookie.headers) // 기존 헤더 유지
            add(HttpHeaders.SET_COOKIE, cookie.toString())
        }

    return ResponseEntity
        .status(this.statusCode)
        .headers(headers)
        .body(this.body)
}
