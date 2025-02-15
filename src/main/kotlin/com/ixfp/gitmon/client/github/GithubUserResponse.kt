package com.ixfp.gitmon.client.github

import com.fasterxml.jackson.annotation.JsonProperty

data class GithubUserResponse(
    @JsonProperty("login") val username: String,
    val id: Number,
    @JsonProperty("avatar_url") val avatarUrl: String,
)
