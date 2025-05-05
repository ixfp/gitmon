package com.ixfp.gitmon.controller.response

data class MemberInfoResponse(
    val id: String,
    val githubUsername: String,
    val isRepoCreated: Boolean,
    val repoName: String?,
)
