package com.ixfp.gitmon.domain.member.exception

import com.ixfp.gitmon.common.aop.ExceptionWrappingStrategy
import com.ixfp.gitmon.common.exception.AppException
import org.springframework.stereotype.Component

@Component
class MemberExceptionStrategy : ExceptionWrappingStrategy {
    override fun wrap(e: Throwable): AppException {
        return when (e) {
            is MemberException -> e
            else -> MemberUnexpectedException(cause = e)
        }
    }
}
