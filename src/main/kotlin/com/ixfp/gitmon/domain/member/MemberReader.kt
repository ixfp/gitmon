package com.ixfp.gitmon.domain.member

import com.ixfp.gitmon.common.aop.WrapWith
import com.ixfp.gitmon.db.exception.DbExceptionStrategy
import com.ixfp.gitmon.db.github.GithubAuthRepository
import com.ixfp.gitmon.db.member.MemberEntity
import com.ixfp.gitmon.db.member.MemberRepository
import org.springframework.stereotype.Component

@WrapWith(DbExceptionStrategy::class)
@Component
class MemberReader(
    private val memberRepository: MemberRepository,
    private val githubAuthRepository: GithubAuthRepository,
) {
    fun findById(id: Long): Member? {
        return memberRepository.findById(id).orElse(null)?.let { MemberEntity.toMember(it) }
    }

    fun findByExposedId(exposedId: String): Member? {
        val memberEntity = memberRepository.findByExposedId(exposedId)
        if (memberEntity != null) return MemberEntity.toMember(memberEntity)
        return null
    }

    fun findByGithubId(githubId: Long): Member? {
        return memberRepository.findByGithubId(githubId)?.let { MemberEntity.toMember(it) }
    }

    fun findByGithubUsername(githubUsername: String): Member? {
        return memberRepository.findByGithubUsername(githubUsername)?.let { MemberEntity.toMember(it) }
    }

    fun findAccessTokenByMemberId(memberId: Long): String? {
        // TODO: 깃허브 토큰만 조회하게 변경
        return githubAuthRepository.findByMemberId(memberId)?.githubAccessToken
    }
}
