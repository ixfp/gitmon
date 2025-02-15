package com.ixfp.gitmon.domain.auth

import com.ixfp.gitmon.client.github.GithubApiService
import com.ixfp.gitmon.common.util.JwtUtil
import com.ixfp.gitmon.db.member.MemberRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val githubApiService: GithubApiService,
    private val jwtUtil: JwtUtil,
    private val memberRepository: MemberRepository
) {
    fun isMember(githubAccessToken: String): Boolean {
        val githubUser = githubApiService.getGithubUser(githubAccessToken)
        val member = memberRepository.findByGithubId(githubUser.id.toLong())
        return member != null
    }

    // TODO: 회원가입
    fun signup() {
        
    }

    // TODO: 로그인

    fun createAccessToken(code: String): String {
        val user = User.from(githubApiService.getGithubUser(code))
        return jwtUtil.createAccessToken(user.name)
    }
}
