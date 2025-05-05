package com.ixfp.gitmon.domain.auth.exception

import com.ixfp.gitmon.common.exception.DomainException

sealed class AuthException(
    message: String,
    cause: Throwable? = null,
) : DomainException(message, cause)

class AuthUnexpectedException(
    message: String = "예상치 못한 인증 도메인 예외가 발생했습니다.",
    cause: Throwable? = null,
) : AuthException(message, cause)
