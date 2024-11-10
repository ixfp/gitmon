package com.ixfp.gitmon.domain.auth

import com.ixfp.gitmon.client.github.GithubUserResponse

data class User(
    val id: Long,
    val name: String,
    val avatarUrl: String,
) {
    companion object {
        fun from(githubUserResponse: GithubUserResponse): User {
            return User(
                id = githubUserResponse.id,
                name = githubUserResponse.username,
                avatarUrl = githubUserResponse.avatarUrl,
            )
        }
    }
}
