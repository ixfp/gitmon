package com.ixfp.gitmon.controller.type

import org.springframework.http.HttpStatus

data class ApiResponseBody<T>(
    val status: HttpStatus,
    val statusCode: Int,
    val data: T? = null,
    val errorMessage: String? = null,
) {
    companion object {
        fun <T> success(): ApiResponseBody<T> {
            return ApiResponseBody(
                status = HttpStatus.OK,
                statusCode = HttpStatus.OK.value(),
            )
        }

        fun <T> success(
            status: HttpStatus = HttpStatus.OK,
            data: T?,
        ): ApiResponseBody<T> {
            return ApiResponseBody(
                status = status,
                statusCode = status.value(),
                data = data,
            )
        }

        fun <T> error(
            error: ApiError,
            data: T? = null,
        ): ApiResponseBody<T> {
            return ApiResponseBody(
                status = error.httpStatus,
                statusCode = error.httpStatus.value(),
                errorMessage = error.message,
                data = data,
            )
        }
    }
}
