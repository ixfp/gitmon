package com.ixfp.gitmon.domain.auth

import com.ixfp.gitmon.client.github.GithubApiService
import com.ixfp.gitmon.client.github.response.GithubUserResponse
import com.ixfp.gitmon.common.util.JwtUtil
import com.ixfp.gitmon.db.github.GithubAuthEntity
import com.ixfp.gitmon.db.github.GithubAuthRepository
import com.ixfp.gitmon.db.member.MemberEntity
import com.ixfp.gitmon.db.member.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthService(
    private val githubApiService: GithubApiService,
    private val jwtUtil: JwtUtil,
    private val memberRepository: MemberRepository,
    private val githubAuthRepository: GithubAuthRepository,
) {
    fun getMemberByGithubId(githubId: Long): MemberEntity? {
        val member = memberRepository.findByGithubId(githubId)
        return member
    }

    @Transactional
    fun signup(
        githubAccessToken: String,
        githubUser: GithubUserResponse,
    ): MemberEntity {
        val memberEntity =
            MemberEntity(
                exposedId = UUID.randomUUID().toString(),
                githubId = githubUser.id.toLong(),
                githubUsername = githubUser.username,
            )
        val savedMember = memberRepository.save(memberEntity)
        val githubAuthEntity = GithubAuthEntity(savedMember, githubAccessToken)
        val savedGithubAuth = githubAuthRepository.save(githubAuthEntity)
        return savedMember
    }

    fun createAccessToken(member: MemberEntity): String {
        return jwtUtil.createAccessToken(member.exposedId)
    }
}
