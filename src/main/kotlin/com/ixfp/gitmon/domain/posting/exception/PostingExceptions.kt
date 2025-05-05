package com.ixfp.gitmon.domain.posting.exception

import com.ixfp.gitmon.common.exception.DomainException

sealed class PostingException(
    message: String,
    cause: Throwable? = null,
) : DomainException(message, cause)

class PostingUnexpectedException(
    message: String = "예상치 못한 게시글 도메인 예외가 발생했습니다.",
    cause: Throwable? = null,
) : PostingException(message, cause)
