package com.ixfp.gitmon.domain.auth

import com.ixfp.gitmon.client.github.response.GithubUserResponse
import com.ixfp.gitmon.common.aop.WrapWith
import com.ixfp.gitmon.common.util.JwtUtil
import com.ixfp.gitmon.domain.auth.exception.AuthExceptionStrategy
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.member.MemberReader
import com.ixfp.gitmon.domain.member.MemberWriter
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@WrapWith(AuthExceptionStrategy::class)
@Service
class AuthService(
    private val jwtUtil: JwtUtil,
    private val memberReader: MemberReader,
    private val memberWriter: MemberWriter,
) {
    fun getMemberByGithubId(githubId: Long): Member? {
        val member = memberReader.findByGithubId(githubId)
        return member
    }

    @Transactional
    fun signup(
        githubAccessToken: String,
        githubUser: GithubUserResponse,
    ): Member {
        val exposedId = UUID.randomUUID().toString()
        val member = memberWriter.save(exposedId, githubUser.id.toLong(), githubUser.username)
        memberWriter.upsertGithubAccessToken(member, githubAccessToken)
        return member
    }

    @Transactional
    fun login(
        githubAccessToken: String,
        member: Member,
    ) {
        memberWriter.upsertGithubAccessToken(member, githubAccessToken)
    }

    fun createAccessToken(member: Member): String {
        return jwtUtil.createAccessToken(member.exposedId)
    }

    fun createRefreshToken(member: Member): String {
        return jwtUtil.createRefreshToken(member.exposedId)
    }

    fun getGithubAccessToken(memberId: Long): String? {
        return memberReader.findAccessTokenByMemberId(memberId)
    }

    fun getMemberByExposedId(memberExposedId: String): Member? {
        return memberReader.findByExposedId(memberExposedId)
    }
}
