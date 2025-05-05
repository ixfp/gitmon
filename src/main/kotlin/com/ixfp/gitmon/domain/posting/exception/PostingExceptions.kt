package com.ixfp.gitmon.domain.posting.exception

import com.ixfp.gitmon.common.exception.DomainException

sealed class PostingException(
    message: String,
    cause: Throwable? = null,
) : DomainException(message, cause)

class PostingRepositoryNotFoundException(
    message: String = "회원의 게시글 레포지토리가 없습니다.",
    cause: Throwable? = null,
) : PostingException(message, cause)

class InvalidImageExtensionException(
    message: String = "업로드하는 파일의 확장자가 이미지가 아닙니다.",
    cause: Throwable? = null,
) : PostingException(message, cause)

class PostingUnexpectedException(
    message: String = "예상치 못한 게시글 도메인 예외가 발생했습니다.",
    cause: Throwable? = null,
) : PostingException(message, cause)
