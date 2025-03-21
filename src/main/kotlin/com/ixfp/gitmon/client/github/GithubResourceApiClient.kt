package com.ixfp.gitmon.client.github

import com.ixfp.gitmon.client.github.request.GithubCreateRepositoryRequest
import com.ixfp.gitmon.client.github.request.GithubUpsertFileRequest
import com.ixfp.gitmon.client.github.response.GithubCreateRepositoryResponse
import com.ixfp.gitmon.client.github.response.GithubFetchRepositoryResponse
import com.ixfp.gitmon.client.github.response.GithubUpsertFileResponse
import com.ixfp.gitmon.client.github.response.GithubUserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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

    @GetMapping(
        "/repos/{owner}/{repo}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun fetchRepository(
        @RequestHeader("Authorization") token: String,
        @RequestHeader("Accept") accept: String = "application/vnd.github+json",
        @RequestHeader("X-GitHub-Api-Version") apiVersion: String = "2022-11-28",
        @PathVariable("owner") owner: String,
        @PathVariable("repo") repo: String,
    ): GithubFetchRepositoryResponse

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

    @PutMapping(
        "/repos/{owner}/{repo}/contents/{path}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun upsertFile(
        @RequestHeader("Authorization") bearerToken: String,
        @RequestHeader("Accept") accept: String = "application/vnd.github+json",
        @RequestHeader("X-GitHub-Api-Version") apiVersion: String = "2022-11-28",
        @PathVariable("owner") owner: String,
        @PathVariable("repo") repo: String,
        @PathVariable("path") path: String,
        @RequestBody request: GithubUpsertFileRequest,
    ): GithubUpsertFileResponse
}
