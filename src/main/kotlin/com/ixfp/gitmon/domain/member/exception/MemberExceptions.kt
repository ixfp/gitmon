package com.ixfp.gitmon.domain.member.exception

import com.ixfp.gitmon.common.exception.DomainException

sealed class MemberException(
    message: String,
    cause: Throwable? = null,
) : DomainException(message, cause)

class MemberUnexpectedException(
    message: String = "예상치 못한 회원 도메인 예외가 발생했습니다.",
    cause: Throwable? = null,
) : MemberException(message, cause)

class MemberNotFoundException(
    message: String = "해당 회원을 찾을 수 없습니다.",
    cause: Throwable? = null,
) : MemberException(message, cause)

class MemberGithubAccessTokenNotFoundException(
    message: String = "해당 회원의 GitHub 액세스 토큰이 존재하지 않습니다.",
    cause: Throwable? = null,
) : MemberException(message, cause)
