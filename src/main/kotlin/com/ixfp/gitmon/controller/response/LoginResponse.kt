package com.ixfp.gitmon.controller.response

data class LoginResponse(
    val id: String,
    val accessToken: String,
    val isRepoCreated: Boolean,
)
