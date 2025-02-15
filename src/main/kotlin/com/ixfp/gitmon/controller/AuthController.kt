package com.ixfp.gitmon.controller

import com.ixfp.gitmon.client.github.GithubCreateRepositoryRequest
import com.ixfp.gitmon.client.github.GithubResourceApiClient
import com.ixfp.gitmon.controller.request.CreateRepoRequest
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.naming.AuthenticationException

@RestController
@RequestMapping("/api/v1")
class AuthController(
    private val githubResourceApiClient: GithubResourceApiClient,
) {
    @PostMapping("/repo")
    fun createRepo(
        @RequestBody request: CreateRepoRequest,
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<HttpStatus> {
        try {
            val token = authorizationHeader.removePrefix("Bearer ")
            val githubRequest =
                GithubCreateRepositoryRequest(
                    name = request.name,
                    description = "Powered By Gitmon",
                    homepage = "https://gitmon.blog",
                    private = false,
                )
            // 깃몬에서 발행한 토큰 파싱

            // DB에서 사용자의 엑세스토큰 조회
            githubResourceApiClient.createRepository("Bearer $token", githubRequest)
            return ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (e: Exception) {
            log.error(e) { "Failed to create repository: ${e.message}" }
            val status =
                when (e) {
                    is IllegalArgumentException -> HttpStatus.BAD_REQUEST
                    is AuthenticationException -> HttpStatus.UNAUTHORIZED
                    else -> HttpStatus.INTERNAL_SERVER_ERROR
                }
            return ResponseEntity.status(status).build()
        }
    }

    companion object {
        private val log = logger {}
    }
}
