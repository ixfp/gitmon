package com.ixfp.gitmon.controller

import com.ixfp.gitmon.client.github.exception.GithubInvalidAuthCodeException
import com.ixfp.gitmon.controller.type.ApiError
import com.ixfp.gitmon.controller.type.ApiResponseBody
import com.ixfp.gitmon.controller.util.ApiResponseHelper
import com.ixfp.gitmon.domain.auth.exception.AuthException
import com.ixfp.gitmon.domain.posting.exception.InvalidImageExtensionException
import com.ixfp.gitmon.domain.posting.exception.PostingRepositoryNotFoundException
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(GithubInvalidAuthCodeException::class)
    fun handleInvalidAuthCodeException(ex: GithubInvalidAuthCodeException): ResponseEntity<ApiResponseBody<String>> {
        return ApiResponseHelper.error(ApiError.INVALID_AUTH_CODE)
    }

    @ExceptionHandler(AuthException::class)
    fun handleInvalidAuthException(ex: AuthException): ResponseEntity<ApiResponseBody<String>> {
        // TODO: Auth 예외 구체화
        return ApiResponseHelper.error(ApiError.UNAUTHORIZED)
    }

    @ExceptionHandler(InvalidImageExtensionException::class)
    fun handleInvalidImageExtensionException(ex: InvalidImageExtensionException): ResponseEntity<ApiResponseBody<String>> {
        return ApiResponseHelper.error(ApiError.BAD_REQUEST, "invalid image extension")
    }

    @ExceptionHandler(PostingRepositoryNotFoundException::class)
    fun handlePostingRepositoryNotFoundException(ex: PostingRepositoryNotFoundException): ResponseEntity<ApiResponseBody<String>> {
        return ApiResponseHelper.error(ApiError.REPOSITORY_NOT_CONFIGURED)
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(ex: Exception): ResponseEntity<ApiResponseBody<Unit>> {
        log.warn(ex) { "예상하지 못한 예외" }
        // TODO: 상황에 따라 error 레벨의 로그 추가
        return ApiResponseHelper.error(ApiError.INTERNAL_SERVER_ERROR)
    }

    companion object {
        private val log = logger {}
    }
}
