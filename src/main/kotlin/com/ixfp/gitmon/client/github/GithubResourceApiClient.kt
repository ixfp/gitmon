package com.ixfp.gitmon.client.github

import com.ixfp.gitmon.client.github.request.GithubCreateRepositoryRequest
import com.ixfp.gitmon.client.github.response.GithubCreateRepositoryResponse
import com.ixfp.gitmon.client.github.response.GithubUserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "github-resource-client", url = "https://api.github.com")
interface GithubResourceApiClient {
    @GetMapping(
        "/user",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun fetchUser(
        @RequestHeader("Authorization") bearerToken: String,
    ): GithubUserResponse

    @PostMapping(
        "/user/repos",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun createRepository(
        @RequestHeader("Authorization") bearerToken: String,
        @RequestBody request: GithubCreateRepositoryRequest,
        @RequestHeader("Accept") accept: String = "application/vnd.github+json",
        @RequestHeader("X-GitHub-Api-Version") apiVersion: String = "2022-11-28",
    ): GithubCreateRepositoryResponse
}
