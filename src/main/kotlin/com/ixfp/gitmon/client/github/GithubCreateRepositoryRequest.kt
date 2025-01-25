package com.ixfp.gitmon.client.github

data class GithubCreateRepositoryRequest(
    val name: String,
    val description: String,
    val homepage: String,
    val private: Boolean
)