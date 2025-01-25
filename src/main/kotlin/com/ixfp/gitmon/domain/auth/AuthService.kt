package com.ixfp.gitmon.domain.auth

import com.ixfp.gitmon.client.github.GithubApiService
import com.ixfp.gitmon.common.util.JwtUtil
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val githubApiService: GithubApiService,
    private val jwtUtil: JwtUtil,
) {
    fun createAccessToken(code: String): String {
        val user = User.from(githubApiService.getUserByCode(code))
        return jwtUtil.createAccessToken(user.name)
    }
}
