package com.ixfp.gitmon.client.github.response

data class GithubUpsertFileResponse(
    val content: GithubContent,
)

data class GithubContent(
    val name: String,
    val path: String,
    val sha: String,
    val download_url: String,
)
