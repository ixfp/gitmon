package com.ixfp.gitmon.client.github.request

data class GithubUpsertFileRequest(
    val message: String,
    val content: String,
    val sha: String,
)
