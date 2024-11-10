package com.ixfp.gitmon.client.github

data class GithubAccessTokenRequest(
    val client_id: String,
    val client_secret: String,
    val code: String,
)
