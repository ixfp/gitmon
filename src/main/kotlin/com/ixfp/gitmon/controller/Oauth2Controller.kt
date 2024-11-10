package com.ixfp.gitmon.controller

import com.ixfp.gitmon.controller.request.GithubOauth2Request
import com.ixfp.gitmon.controller.response.AccessTokenResponse
import com.ixfp.gitmon.domain.auth.AuthService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.naming.AuthenticationException

@RestController
@RequestMapping("/api/v1")
class Oauth2Controller(
    @Value("\${oauth2.client.github.id}") private val githubClientId: String,
    private val authService: AuthService,
) {
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
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(response)
        } catch (e: Exception) {
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
