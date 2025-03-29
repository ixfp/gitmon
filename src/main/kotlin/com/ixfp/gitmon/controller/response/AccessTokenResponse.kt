package com.ixfp.gitmon.controller.response

data class AccessTokenResponse(
    val id: String,
    val accessToken: String,
    val isRepoCreated: Boolean,
)
