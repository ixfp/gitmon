package com.ixfp.gitmon.client.github.exception

import com.ixfp.gitmon.common.exception.AppException

sealed class GithubApiException(
    message: String,
    cause: Throwable? = null,
) : AppException(message, cause)

class GithubNetworkException(
    message: String = "GitHub 서버와의 네트워크 통신에 실패했습니다.",
    cause: Throwable? = null,
) : GithubApiException(message, cause)

class GithubResponseParsingException(
    message: String = "GitHub 응답을 파싱하는 데 실패했습니다.",
    cause: Throwable? = null,
) : GithubApiException(message, cause)

class GithubUnexpectedException(
    message: String = "GitHub API 호출 중 예상치 못한 예외가 발생했습니다.",
    cause: Throwable? = null,
) : GithubApiException(message, cause)

class GithubInvalidAuthCodeException(
    message: String = "Github Auth Code가 유효하지 않습니다.",
    cause: Throwable? = null,
) : GithubApiException(message, cause)
