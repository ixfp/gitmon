package com.ixfp.gitmon.controller.util

import com.ixfp.gitmon.controller.type.ApiError
import com.ixfp.gitmon.controller.type.ApiResponseBody
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

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
