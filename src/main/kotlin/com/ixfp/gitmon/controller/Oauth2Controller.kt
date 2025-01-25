package com.ixfp.gitmon.controller

import com.ixfp.gitmon.client.github.GithubAccessTokenRequest
import com.ixfp.gitmon.client.github.GithubCreateRepositoryRequest
import com.ixfp.gitmon.client.github.GithubOauth2ApiClient
import com.ixfp.gitmon.client.github.GithubResourceApiClient
import com.ixfp.gitmon.controller.request.GithubOauth2Request
import com.ixfp.gitmon.controller.response.AccessTokenResponse
import com.ixfp.gitmon.domain.auth.AuthService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.naming.AuthenticationException

@RestController
@RequestMapping("/api/v1")
@Tag(name = "OAuth Token Control", description = "OAuth 인증 관련 API")
class Oauth2Controller(
    @Value("\${oauth2.client.github.id}") private val githubClientId: String,
    private val authService: AuthService,
    private val githubResourceApiClient: GithubResourceApiClient, // 임시
) {
    @Operation(summary = "Get Oauth2 Token", description = "Github으로부터 Oauth2 Token을 받아오는 API")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Success", content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = ArraySchema(schema = Schema(implementation = String::class))
                )]
            ),
        ]
    )
    @GetMapping("/login/oauth/github")
    fun redirectToGithubOauthUrl(): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(
            "Location",
            "https://github.com/login/oauth/authorize?client_id=$githubClientId",
        ).build()
    }

    @PostMapping("/login/oauth/github/tokens")
    fun login(
        @RequestBody request: GithubOauth2Request,
    ): ResponseEntity<AccessTokenResponse> {
        try {
            val accessToken = authService.createAccessToken(request.code)
            val response = AccessTokenResponse(accessToken)
            return ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: Exception) {
            log.info { "Failed to login: ${e.message}" }
            val status = when (e) {
                is IllegalArgumentException -> HttpStatus.BAD_REQUEST
                is AuthenticationException -> HttpStatus.UNAUTHORIZED
                else -> HttpStatus.INTERNAL_SERVER_ERROR
            }
            return ResponseEntity.status(status).build()
        }
    }

    // 임시
    @GetMapping("/login/oauth/github/sign_up")
    fun signUp(): ResponseEntity<Any> {
        try {
            val accessToken =
                "fill this access token"
            val request = GithubCreateRepositoryRequest(
                name = "foo",
                description = "bar",
                homepage = "lorem ipsum",
                private = false
            )
            githubResourceApiClient.createRepository(accessToken, request)
            return ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (e: Exception) {
            println(e.message)
            log.info { "Failed to login: ${e.message}" }
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
