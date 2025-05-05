package com.ixfp.gitmon.client.github.exception

import com.fasterxml.jackson.core.JsonProcessingException
import com.ixfp.gitmon.common.aop.ExceptionWrappingStrategy
import com.ixfp.gitmon.common.exception.AppException
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class GithubApiExceptionStrategy : ExceptionWrappingStrategy {
    override fun wrap(e: Throwable): AppException =
        when (e) {
            is GithubApiException -> e
            is JsonProcessingException -> GithubResponseParsingException(cause = e)
            is IOException -> GithubNetworkException(cause = e)
            else -> GithubUnexpectedException(cause = e)
        }
}
