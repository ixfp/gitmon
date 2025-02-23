package com.ixfp.gitmon.client.github.request

data class GithubCreateRepositoryRequest(
    val name: String,
    val description: String,
    val homepage: String,
    val private: Boolean,
)
