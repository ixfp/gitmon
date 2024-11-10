package com.ixfp.gitmon.domain.auth

import com.ixfp.gitmon.client.github.GithubApiClient
import com.ixfp.gitmon.common.util.JwtUtil
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val githubApiClient: GithubApiClient,
    private val jwtUtil: JwtUtil,
) {
    fun createAccessToken(code: String): String {
        val user = User.from(githubApiClient.getUserByCode(code))
        return jwtUtil.createAccessToken(user.name)
    }
}
