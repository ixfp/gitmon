package com.ixfp.gitmon.domain.auth.exception

import com.ixfp.gitmon.common.aop.ExceptionWrappingStrategy
import com.ixfp.gitmon.common.exception.AppException
import org.springframework.stereotype.Component

@Component
class AuthExceptionStrategy : ExceptionWrappingStrategy {
    override fun wrap(e: Throwable): AppException {
        return when (e) {
            is AuthException -> e
            else -> AuthUnexpectedException(cause = e)
        }
    }
}
