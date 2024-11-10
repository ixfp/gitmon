package com.ixfp.gitmon.client.github

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "github-oauth2-client", url = "https://github.com/login/oauth")
interface GithubOauth2ApiClient {
    @PostMapping(
        "/access_token",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun fetchAccessToken(
        @RequestBody body: GithubAccessTokenRequest,
    ): GithubAccessTokenResponse
}
