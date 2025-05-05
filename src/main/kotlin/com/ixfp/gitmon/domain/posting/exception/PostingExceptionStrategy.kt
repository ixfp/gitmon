package com.ixfp.gitmon.domain.posting.exception

import com.ixfp.gitmon.common.aop.ExceptionWrappingStrategy
import com.ixfp.gitmon.common.exception.AppException
import org.springframework.stereotype.Component

@Component
class PostingExceptionStrategy : ExceptionWrappingStrategy {
    override fun wrap(e: Throwable): AppException {
        return when (e) {
            is AppException -> e
            else -> PostingUnexpectedException(cause = e)
        }
    }
}
