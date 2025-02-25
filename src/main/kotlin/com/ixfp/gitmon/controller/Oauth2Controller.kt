package com.ixfp.gitmon.controller

import com.ixfp.gitmon.client.github.GithubApiService
import com.ixfp.gitmon.controller.request.GithubOauth2Request
import com.ixfp.gitmon.controller.response.AccessTokenResponse
import com.ixfp.gitmon.domain.auth.AuthService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
    private val githubApiService: GithubApiService,
) {
    @Operation(summary = "Get Oauth2 Token", description = "Github으로부터 Oauth2 Token을 받아오는 API")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = ArraySchema(schema = Schema(implementation = String::class)),
                    ),
                ],
            ),
        ],
    )
    @GetMapping("/login/oauth/github")
    fun redirectToGithubOauthUrl(): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(
            "Location",
            "https://github.com/login/oauth/authorize?&scope=repo&client_id=$githubClientId",
        ).build()
    }

    @PostMapping("/login/oauth/github/tokens")
    fun login(
        @RequestBody request: GithubOauth2Request,
    ): ResponseEntity<AccessTokenResponse> {
        try {
            val githubAccessToken = githubApiService.getAccessTokenByCode(request.code)
            val githubUser = githubApiService.getGithubUser(githubAccessToken)
            var member = authService.getMemberByGithubId(githubUser.id.toLong())
            if (member === null) {
                member = authService.signup(githubAccessToken, githubUser)
            }
            authService.login(githubAccessToken, member)
            val accessToken = authService.createAccessToken(member)
            // TODO: 레포지토리 생성여부 조회
            val response = AccessTokenResponse(accessToken, false)
            return ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: Exception) {
            log.error(e) { "Failed to login: ${e.message}" }
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
