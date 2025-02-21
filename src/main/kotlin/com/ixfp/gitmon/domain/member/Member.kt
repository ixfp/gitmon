package com.ixfp.gitmon.domain.member

data class Member(
    val id: Long,
    val exposedId: String,
    val githubId: Long,
    val githubUsername: String,
    val repoName: String?,
)
