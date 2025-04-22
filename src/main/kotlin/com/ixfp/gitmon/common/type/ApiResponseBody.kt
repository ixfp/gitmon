package com.ixfp.gitmon.common.type

data class ApiResponseBody<T>(
    val status: ApiStatus,
    val data: T? = null,
    val error: ApiErrorType? = null,
) {
    companion object {
        fun <T> success(): ApiResponseBody<T> {
            return ApiResponseBody(
                status = ApiStatus.SUCCESS,
            )
        }

        fun <T> success(data: T): ApiResponseBody<T> {
            return ApiResponseBody(
                status = ApiStatus.SUCCESS,
                data = data,
            )
        }

        fun <T> error(error: ApiErrorType): ApiResponseBody<T> {
            return ApiResponseBody(
                status = error.status,
                error = error,
            )
        }
    }
}

enum class ApiStatus {
    SUCCESS,
    UNAUTHORIZED,
    FORBIDDEN,
    ERROR,
}

enum class ApiErrorType(val status: ApiStatus, message: String) {
    BAD_REQUEST(ApiStatus.ERROR, "Bad request"),
    UNAUTHORIZED(ApiStatus.UNAUTHORIZED, "Unauthorized"),
    INVALID_ACCESS_TOKEN(ApiStatus.UNAUTHORIZED, "Invalid access token"),
    EXPIRED_ACCESS_TOKEN(ApiStatus.UNAUTHORIZED, "Access token expired"),

    INTERNAL_SERVER_ERROR(ApiStatus.ERROR, "Internal server error"),
}
