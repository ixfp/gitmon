package com.ixfp.gitmon.client.github.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GithubAccessTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    val scope: String,
    @JsonProperty("token_type")
    val tokenType: String,
)
